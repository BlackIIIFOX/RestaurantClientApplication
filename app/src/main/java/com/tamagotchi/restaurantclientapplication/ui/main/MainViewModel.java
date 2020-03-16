package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.LoginDataSource;
import com.tamagotchi.restaurantclientapplication.data.LoginRepository;

public class MainViewModel extends ViewModel {
    public void StartServices() {
        LoginRepository login = LoginRepository.getInstance(new LoginDataSource());
        login.isLoggedIn();
    }
}
