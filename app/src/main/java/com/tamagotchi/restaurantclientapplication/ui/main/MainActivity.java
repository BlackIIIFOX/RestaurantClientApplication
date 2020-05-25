package com.tamagotchi.restaurantclientapplication.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.menu.MenuFragment;
import com.tamagotchi.restaurantclientapplication.ui.orders.OrdersFragment;
import com.tamagotchi.restaurantclientapplication.ui.restaurants.RestaurantsFragment;
import com.tamagotchi.restaurantclientapplication.ui.still.StillFragment;

import ru.yandex.money.android.sdk.Checkout;
import ru.yandex.money.android.sdk.TokenizationResult;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private BottomNavigationView bottomNavigationView;

    private RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
    private MenuFragment menuFragment = new MenuFragment();
    private OrdersFragment ordersFragment = new OrdersFragment();
    private StillFragment stillFragment = new StillFragment();
    private MainViewModel viewModel;
    private Navigation previousNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        InitNavigation();
    }

    private void InitNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Подписываемся на изменения из ViewModel.
        viewModel.getSelectedNavigation().observe(this, selectedNavigation -> {
            if (previousNavigation == selectedNavigation)
                return;

            switch (selectedNavigation) {
                case Menu:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();
                    break;
                case Order:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_orders);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, ordersFragment).commit();
                    break;
                case Options:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_still);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, stillFragment).commit();
                    break;
                case Restaurant:
                    bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, restaurantsFragment).commit();
                    break;
            }

            previousNavigation = selectedNavigation;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_restaurants:
                if (isServicesOK()) {
                    viewModel.setSelectedNavigation(Navigation.Restaurant);
                }
                return true;

            case R.id.navigation_menu:
                viewModel.setSelectedNavigation(Navigation.Menu);
                return true;

            case R.id.navigation_orders:
                viewModel.setSelectedNavigation(Navigation.Order);
                return true;

            case R.id.navigation_still:
                viewModel.setSelectedNavigation(Navigation.Options);
                return true;
        }

        return false;
    }

    public boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            return true; //Services is working
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK error: an error occured but we can fix it!");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Результат оплаты.
        if (requestCode == OrdersFragment.REQUEST_CODE_TOKENIZE) {
            switch (resultCode) {
                case RESULT_OK:
                    // successful tokenization
                    // Токен сформирован, дальше нужно отправить его на сервер и ждать результат,
                    // но для этого надо подключить яндекс кассы (быть ИП или индивидуальным предпринимателем).
                    TokenizationResult result = Checkout.createTokenizationResult(data);
                    result.getPaymentToken();
                    viewModel.setSelectedNavigation(Navigation.Options);
                    break;
                case RESULT_CANCELED:
                    // user canceled tokenization
                    break;
            }
        }
    }
}
