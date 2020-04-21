package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;

public class StartViewModelFactory implements ViewModelProvider.Factory  {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StartViewModel.class)) {
            return (T) new StartViewModel(
                    AuthenticationService.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
