package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public void StartServices() {
        //AccountsRepository login = AccountsRepository.getInstance();

        /*
        AccountModel newAccount = new AccountModel();
        newAccount.setLogin("test");
        newAccount.setPassword("test");
        newAccount.setRole(Roles.Client);

        RestaurantClient.getInstance().getAccountService().createAccount(newAccount).enqueue(new Callback<AccountModel>() {
            @Override
            public void onResponse(Call<AccountModel> call, Response<AccountModel> response) {
                if(response.isSuccessful()) {
                    AccountModel account = response.body();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AccountModel> call, Throwable t) {
                t.printStackTrace();
            }
        });*/

        /*
        RestaurantClient.getInstance().getAccountService().getAccounts().enqueue(new Callback<List<AccountModel>>() {
            @Override
            public void onResponse(Call<List<AccountModel>> call, Response<List<AccountModel>> response) {
                if(response.isSuccessful()) {
                    List<AccountModel> accounts = response.body();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<AccountModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
    }
}
