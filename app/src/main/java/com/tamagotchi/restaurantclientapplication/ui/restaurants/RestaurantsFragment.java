package com.tamagotchi.restaurantclientapplication.ui.restaurants;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.ui.main.MainActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;
import com.tamagotchi.restaurantclientapplication.ui.menu.MenuFragment;
import com.tamagotchi.restaurantclientapplication.ui.slidingpanel.SlidingPanelFragment;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RestaurantsFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "RestaurantsFragment";

    private static final String FINE_LICATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Integer lastSelectedMarker = null;

    private Boolean mLocationPermissionsGranted = false;
    private Boolean mGPSPermissionsGranted = false;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private MainViewModel viewModel;

    public RestaurantsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);

        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getLocationPermission();

        return root;
    }

    public boolean isGeoEnabled() {
        LocationManager mLocationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        mGPSPermissionsGranted = mIsGPSEnabled && mIsNetworkEnabled;
        return mGPSPermissionsGranted;
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(RestaurantsFragment.this.requireContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMap();

        if (mLocationPermissionsGranted) { //TODO: Тестовая версия, раскоментить в релизе
//            if (!isGeoEnabled()) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.9386300, 30.3141300), 10f));
//                return;
//            }
//
//            getDeviceLocation();
//
//            if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
        }
    }

    private void updateMap() {
        viewModel.getRestaurants().observe(getViewLifecycleOwner(), result ->
        {
            if (result instanceof Result.Success) {
                List<RestaurantModel> restaurants = (List<RestaurantModel>) ((Result.Success) result).getData();
                LatLng restaurantPos;

                HashMap<Integer, Marker> markers = new HashMap();
                for (int i = 0; i < restaurants.size(); i++) {
                    RestaurantModel restaurant = restaurants.get(i);

                    restaurantPos = new LatLng(restaurant.getPositionLatitude(), restaurant.getPositionLongitude());

                    MarkerOptions markerOptions = new MarkerOptions().position(restaurantPos).title(restaurant.getAddress());
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    Marker restaurantMarker = mMap.addMarker(markerOptions);
                    restaurantMarker.setTag(restaurant); // Добавляем объект рестарана в качестве тега.
                    markers.put(restaurant.getId(), restaurantMarker);
                }

                // Подписываемся на обновление выбранного маркера.
                viewModel.getSelectedRestaurant().observe(this, selectedRestaurant -> {
                    if (selectedRestaurant == null)
                        return;

                    // Сбрасываем цвет прошлого маркера
                    if (lastSelectedMarker != null) {
                        Marker previous = markers.get(lastSelectedMarker);

                        if (previous != null)
                            previous.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Устанавливаем зеленый цвет на текущий маркер.
                    Marker selected = markers.get(selectedRestaurant.getId());

                    if (selected != null) {
                        selected.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        lastSelectedMarker = selectedRestaurant.getId();
                    }
                });
            } else {
                // TODO: обработка ошибки
            }
        });

        mMap.setOnMarkerClickListener(this);
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.requireContext(), FINE_LICATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.requireContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this.requireActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        mLocationPermissionsGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grandResults.length > 0) {
                for (int grandResult : grandResults) {
                    if (grandResult == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        break;
                    }
                }
                mLocationPermissionsGranted = true; //initialization our map
                initMap();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Получаем выбранный ресторан и устанавливаем его в ViewModel
        RestaurantModel currentRestaurant = (RestaurantModel) marker.getTag();
        viewModel.setSelectedRestaurant(currentRestaurant);

        SlidingPanelFragment slidingPanelFragment = new SlidingPanelFragment();
        slidingPanelFragment.show(requireActivity().getSupportFragmentManager(), "");

        return true;
    }
}
