package com.tamagotchi.restaurantclientapplication.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAuthenticateApiService;
import com.tamagotchi.tamagotchiserverprotocol.services.IAuthenticateInfoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationService {

    private MutableLiveData<Result> isAuth = new MutableLiveData<>();
    private IAuthenticateApiService authenticateApiService;
    private IAuthenticateInfoService authenticateInfoService;

    public LiveData<Result> isAuthenticated() {
        checkAuthenticate();
        return isAuth;
    }

    public AuthenticationService(IAuthenticateApiService authenticateApiService, IAuthenticateInfoService authenticateInfoService) {
        this.authenticateApiService = authenticateApiService;
        this.authenticateInfoService = authenticateInfoService;
        checkAuthenticate();
    }

    public LiveData<Result> authenticate(LoginInfo loginInfo) {
        SignInfoModel loginInfoModel = new SignInfoModel(loginInfo.getLogin(),
                loginInfo.getPassword().getPasswordMd5());

        this.authenticateApiService.authenticate(loginInfoModel).enqueue(new Callback<AuthenticateInfoModel>() {
            @Override
            public void onResponse(Call<AuthenticateInfoModel> call, Response<AuthenticateInfoModel> response) {
                if (response.isSuccessful()) {
                    AuthenticateInfoModel authenticateInfoModel = response.body();

                    authenticateInfoService.LogIn(authenticateInfoModel);
                    // TODO: сделать сохранение jwt локально

                    isAuth.setValue(new Result.Success());
                }
            }

            @Override
            public void onFailure(Call<AuthenticateInfoModel> call, Throwable t) {
                isAuth.setValue(new Result.Error(new Exception(t)));
            }
        });

        return isAuth;
    }

    public void checkAuthenticate() {
        // TODO: нужно проверять локальное хранилище jwt

        if (authenticateInfoService.isAuthenticate()) {
            isAuth.setValue(new Result.Success());
        } else {
            isAuth.setValue(new Result.Error(new Exception()));
        }
    }


}
