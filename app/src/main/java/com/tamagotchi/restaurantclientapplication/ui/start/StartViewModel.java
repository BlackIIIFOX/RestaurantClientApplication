package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthLoginException;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;

public class StartViewModel extends ViewModel {

    private AuthenticationService authenticationService;
    private MutableLiveData<Result> isAuthenticated = new MutableLiveData<>();

    StartViewModel(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        this.authenticationService.isAuthenticated().subscribe(
                isAuth -> {
                    if (isAuth) {
                        isAuthenticated.setValue(new Result.Success());
                    } else {
                        isAuthenticated.setValue(new Result.Error(new AuthLoginException()));
                    }
                }, error -> {
                    isAuthenticated.setValue(new Result.Error(new AuthLoginException()));
                }
        );
    }

    public LiveData<Result> isAuthenticated() {
        return isAuthenticated;
    }

    /*public void refreshAuthenticated() {
        authenticationService.checkAuthenticate();
    }*/

}
