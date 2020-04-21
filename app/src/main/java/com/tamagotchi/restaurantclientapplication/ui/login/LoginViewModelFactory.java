package com.tamagotchi.restaurantclientapplication.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;

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
            return (T) new LoginViewModel(
                    AccountsRepository.getInstance(RestaurantClient.getInstance().getAccountService()),
                    AuthenticationService.getInstance()
            );
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
