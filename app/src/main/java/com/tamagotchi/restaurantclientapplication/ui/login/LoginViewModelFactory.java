package com.tamagotchi.restaurantclientapplication.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.tamagotchi.restaurantclientapplication.data.repositories.UsersRepository;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private static LoginViewModel viewModel;

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public synchronized  <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            if (viewModel == null) {
                viewModel = new LoginViewModel(
                        UsersRepository.getInstance(),
                        AuthenticationService.getInstance()
                );
            }

            return (T) viewModel;
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
