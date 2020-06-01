package com.tamagotchi.restaurantclientapplication.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.ui.menu.MenuFragment;
import com.tamagotchi.restaurantclientapplication.ui.orders.OrdersFragment;
import com.tamagotchi.restaurantclientapplication.ui.restaurants.RestaurantsFragment;
import com.tamagotchi.restaurantclientapplication.ui.still.StillFragment;
import com.tamagotchi.tamagotchiserverprotocol.models.ErrorResponse;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
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

        Menu navigationMenu = bottomNavigationView.getMenu();
        MenuItem restaurantMenuItem = navigationMenu.getItem(0);
        MenuItem menuMenuItem = navigationMenu.getItem(1);
        MenuItem orderMenuItem = navigationMenu.getItem(2);
        MenuItem optionsMenuItem = navigationMenu.getItem(3);

        // Подписываемся на изменения из ViewModel.
        viewModel.getSelectedNavigation().observe(this, selectedNavigation -> {
            if (previousNavigation == selectedNavigation)
                return;

            switch (selectedNavigation) {
                case Menu:
                    if (checkSelectedRestaurant()) {
                        //bottomNavigationView.setSelectedItemId(R.id.navigation_menu);
                        menuMenuItem.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).commit();
                    }
                    else {
                        restaurantMenuItem.setChecked(true);
                        //bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);
                        viewModel.setSelectedNavigation(Navigation.Restaurant);
                        return;
                    }

                    break;
                case Order:
                    if (checkSelectedRestaurant()) {
                        orderMenuItem.setChecked(true);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_orders);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, ordersFragment).commit();
                    }
                    else {
                        restaurantMenuItem.setChecked(true);
                        //bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);
                        viewModel.setSelectedNavigation(Navigation.Restaurant);
                        return;
                    }

                    break;
                case Options:
                    optionsMenuItem.setChecked(true);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_still);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, stillFragment).commit();
                    break;
                case Restaurant:
                    restaurantMenuItem.setChecked(true);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_restaurants);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, restaurantsFragment).commit();
                    break;
            }

            previousNavigation = selectedNavigation;
        });
    }

    /**
     * Проверяет возможность переключения элементов меню "Меню" и "Заказ".
     * Если ни один ресторанр не выбран на текущий момент, то выводит ошибку.
     * @return true - смена элементов меню на "Меню" или "Заказ" возможна.
     */
    private boolean checkSelectedRestaurant() {
        if (viewModel.getSelectedRestaurant().getValue() == null) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle(R.string.error)
                    .setMessage(R.string.primarily_select_restaurant)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {

                    }).show();

            return false;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_restaurants:
                if (isServicesOK()) {
                    viewModel.setSelectedNavigation(Navigation.Restaurant);
                }
                break;

            case R.id.navigation_menu:
                viewModel.setSelectedNavigation(Navigation.Menu);
                break;

            case R.id.navigation_orders:
                viewModel.setSelectedNavigation(Navigation.Order);
                break;

            case R.id.navigation_still:
                viewModel.setSelectedNavigation(Navigation.Options);
                break;
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

                    String token = result.getPaymentToken();

                    viewModel.doOrder(token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                Toast.makeText(this, R.string.orderSuccessCreated, Toast.LENGTH_LONG).show();
                                viewModel.getUserMenu().observe(this.ordersFragment.getViewLifecycleOwner(), List::clear);
                                },
                                    error -> {
                                        String textError = error.toString();
                                        if (error instanceof HttpException) {
                                            ResponseBody body = ((HttpException) error).response().errorBody();

                                            Gson gson = new Gson();
                                            TypeAdapter<ErrorResponse> adapter = gson.getAdapter
                                                    (ErrorResponse.class);

                                            try {
                                                ErrorResponse errorParser =
                                                        adapter.fromJson(body.string());

                                                textError = errorParser.getMessage();

                                            } catch (IOException ignored) {
                                            }
                                        }

                                        Toast.makeText(this,
                                                getResources().getString(R.string.orderErrorCreated) + "(" + textError + ")", Toast.LENGTH_LONG)
                                                .show();
                                    });

                    viewModel.setSelectedNavigation(Navigation.Restaurant);
                    break;
                case RESULT_CANCELED:
                    // user canceled tokenization
                    break;
            }
        }
    }
}
