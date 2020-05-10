package com.tamagotchi.restaurantclientapplication.ui.restaurants;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.List;
import java.util.Objects;

public class RestaurantsFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "RestaurantsFragment";

    private static final String FINE_LICATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;
    private Boolean mGPSPermissionsGranted = false;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private RestaurantsViewModel restaurantsViewModel;

    public RestaurantsFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restaurantsViewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getLocationPermission();
        initMap();

        return root;
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());

        try {
            if (mLocationPermissionsGranted) {
                Task location =  mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RestaurantsFragment.this.requireContext(),"unable to get current location", Toast.LENGTH_SHORT).show();
                        }
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
        mMap = googleMap; //Добавать метки на карту
        updateMap();

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mGPSPermissionsGranted = true;
        }
    }

    private void updateMap() {
        restaurantsViewModel.getRestaurants().observe(getViewLifecycleOwner(), result ->
        {
            if (result instanceof Result.Success) {
                List<RestaurantModel> restaurants = (List<RestaurantModel>)((Result.Success) result).getData();

                LatLng restaurantPos = new LatLng(59.9386300, 30.3141300);
                for (int i = 0; i < restaurants.size(); i++) {
                    RestaurantModel restaurant = restaurants.get(i);

                    // Add a marker in Sydney and move the camera
                    restaurantPos = new LatLng(restaurant.getPositionLatitude(), restaurant.getPositionLongitude());
                    mMap.addMarker(new MarkerOptions().position(restaurantPos).title(restaurant.getAddress()));
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantPos, 15));
            } else {
                // TODO: обработка ошибки
            }
        });
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.requireContext(), FINE_LICATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.requireContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                //initMap();    //Тут подключаем положение телефона
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
                //initMap();    //Тут подключаем положение телефона
            }
        }
    }
}
