package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;

public class StartViewModel extends ViewModel {

    private AuthenticationService authenticationService;

    StartViewModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public LiveData<Result> isAuthenticated() {
        return authenticationService.isAuthenticated();
    }

    public void refreshAuthenticated() {
        authenticationService.checkAuthenticate();
    }

}
