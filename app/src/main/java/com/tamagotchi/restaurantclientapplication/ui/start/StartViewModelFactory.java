package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;

public class StartViewModelFactory implements ViewModelProvider.Factory  {
    private static StartViewModel viewModel;

    @NonNull
    @Override
    public synchronized  <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StartViewModel.class)) {
            if (viewModel == null) {
                viewModel = new StartViewModel(
                        AuthenticationService.getInstance());
            }

            return (T) viewModel;
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
