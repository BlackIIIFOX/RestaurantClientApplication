package com.tamagotchi.restaurantclientapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamagotchi.restaurantclientapplication.data.model.LoggedInUser;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAccountsApiService;
import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountsRepository {

    private static volatile AccountsRepository instance;

    private LoggedInUser user = null;
    private IAccountsApiService accountsApiService;
    private AccountsDAO accountsDAO;

    // private constructor : singleton access
    private AccountsRepository(IAccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    public synchronized static AccountsRepository getInstance(IAccountsApiService accountsApiService) {
        if (instance == null) {
            instance = new AccountsRepository(accountsApiService);
        }

        return instance;
    }

    public LiveData<Result> createAccount(SignInfoModel signUpInfo) {
        if (signUpInfo.getLogin().isEmpty() || signUpInfo.getPassword().isEmpty()) {
            throw new IllegalArgumentException("No login or password");
        }

        MutableLiveData<Result> newAccountLivaData = new MutableLiveData<>();
        accountsApiService.createAccount(signUpInfo).enqueue(new Callback<AccountInfoModel>() {
            @Override
            public void onResponse(Call<AccountInfoModel> call, Response<AccountInfoModel> response) {
                if (response.isSuccessful()) {
                    newAccountLivaData.setValue(new Result.Success());
                }
            }

            @Override
            public void onFailure(Call<AccountInfoModel> call, Throwable t) {
                // TODO
                newAccountLivaData.setValue(new Result.Error(new Exception(t)));
            }
        });

        return newAccountLivaData;
    }

    private LiveData<AccountInfoModel> getCurrantAccount() {
        return null;
    }

}
