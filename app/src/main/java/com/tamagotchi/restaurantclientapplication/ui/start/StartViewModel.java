package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;

public class StartViewModel extends ViewModel {

    private AccountsRepository accountsRepository;

    StartViewModel(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

}
