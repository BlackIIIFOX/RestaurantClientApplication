package com.tamagotchi.restaurantclientapplication.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.menu.MenuFragment;
import com.tamagotchi.restaurantclientapplication.ui.orders.OrdersFragment;
import com.tamagotchi.restaurantclientapplication.ui.restaurants.RestaurantsFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    MenuFragment menuFragment = new MenuFragment();
    OrdersFragment ordersFragment = new OrdersFragment();
    RestaurantsFragment restaurantsFragment = new RestaurantsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_restaurants:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, restaurantsFragment).commit();
                return true;

            case R.id.navigation_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();
                return true;

            case R.id.navigation_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, ordersFragment).commit();
                return true;
        }

        return false;
    }
}
