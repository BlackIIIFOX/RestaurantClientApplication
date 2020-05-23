package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tamagotchi.restaurantclientapplication.data.repositories.DishesRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.MenuRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.restaurantclientapplication.ui.start.StartViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private static MainViewModel viewModel;

    @NonNull
    @Override
    public synchronized <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            if (viewModel == null) {
                viewModel = new MainViewModel(
                        RestaurantsRepository.getInstance(),
                        DishesRepository.getInstance(),
                        MenuRepository.getInstance()
                        );
            }

            return (T) viewModel;
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
