package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.LoginDataSource;
import com.tamagotchi.restaurantclientapplication.data.LoginRepository;
import com.tamagotchi.tamagotchiserverprotocol.IRestaurantApiService;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;
import com.tamagotchi.tamagotchiserverprotocol.models.AccountModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainViewModel extends ViewModel {
    public void StartServices() {
        LoginRepository login = LoginRepository.getInstance(new LoginDataSource());
        login.isLoggedIn();
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

    }
}
