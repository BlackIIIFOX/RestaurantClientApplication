package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.LoginDataSource;
import com.tamagotchi.restaurantclientapplication.data.LoginRepository;
import com.tamagotchi.tamagotchiserverprotocol.IRestaurantApiService;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantApiService;

public class MainViewModel extends ViewModel {
    public void StartServices() {
        IRestaurantApiService restaurantApiService = new RestaurantApiService();

        LoginRepository login = LoginRepository.getInstance(new LoginDataSource(restaurantApiService));
        login.isLoggedIn();
    }
}
