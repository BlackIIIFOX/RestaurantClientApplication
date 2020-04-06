package com.tamagotchi.restaurantclientapplication.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.tamagotchi.restaurantclientapplication.data.LoginDataSource;
import com.tamagotchi.restaurantclientapplication.data.LoginRepository;
import com.tamagotchi.tamagotchiserverprotocol.IRestaurantApiService;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantApiService;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource(new RestaurantApiService())));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
