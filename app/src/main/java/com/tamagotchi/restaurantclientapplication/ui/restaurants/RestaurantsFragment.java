package com.tamagotchi.restaurantclientapplication.ui.restaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.List;

public class RestaurantsFragment extends Fragment implements OnMapReadyCallback {

    private RestaurantsViewModel restaurantsViewModel;
    private GoogleMap mMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        restaurantsViewModel = new ViewModelProvider(this).get(RestaurantsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);

        //final TextView textView = root.findViewById(R.id.text_restaurants);

        mapInitialize();

//        restaurantsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    private void mapInitialize() {
        FragmentManager fm = this.getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UpdateMap();
    }

    private void UpdateMap() {
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
}
