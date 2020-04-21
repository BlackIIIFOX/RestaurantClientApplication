package com.tamagotchi.restaurantclientapplication.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthLoginException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAuthenticateApiService;
import com.tamagotchi.tamagotchiserverprotocol.services.IAuthenticateInfoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationService {

    private static final Object syncInstance = new Object();
    private static AuthenticationService instance;

    private MutableLiveData<Result> isAuth = new MutableLiveData<>();
    private IAuthenticateApiService authenticateApiService;
    private IAuthenticateInfoService authenticateInfoService;

    public LiveData<Result> isAuthenticated() {
        return isAuth;
    }

    private AuthenticationService(IAuthenticateApiService authenticateApiService, IAuthenticateInfoService authenticateInfoService) {
        this.authenticateApiService = authenticateApiService;
        this.authenticateInfoService = authenticateInfoService;
    }

    public static AuthenticationService getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public synchronized static void InitializeService(IAuthenticateApiService authenticateApiService, IAuthenticateInfoService authenticateInfoService) {
        synchronized (syncInstance) {
            instance = new AuthenticationService(authenticateApiService, authenticateInfoService);
        }
    }

    public LiveData<Result> authenticate(LoginInfo loginInfo) {
        SignInfoModel loginInfoModel = new SignInfoModel(loginInfo.getLogin(),
                loginInfo.getPassword().getPasswordMd5());

        // Сбрасываем предыдущее значение, т.к. оно вернеться подписчикам.
        isAuth.setValue(null);
        authenticateInfoService.LogOut();

        this.authenticateApiService.authenticate(loginInfoModel).enqueue(new Callback<AuthenticateInfoModel>() {
            @Override
            public void onResponse(Call<AuthenticateInfoModel> call, Response<AuthenticateInfoModel> response) {
                if (response.isSuccessful()) {
                    AuthenticateInfoModel authenticateInfoModel = response.body();
                    authenticateInfoService.LogIn(authenticateInfoModel);
                    // TODO: сделать сохранение jwt локально
                    Result result = new Result.Success<Boolean>(true);
                    isAuth.setValue(result);
                } else {
                    switch (response.code()) {
                        case 401:
                            isAuth.setValue(new Result.Error(new AuthPasswordException()));
                            break;
                        case 404:
                            isAuth.setValue(new Result.Error(new AuthLoginException()));
                            break;
                        default:
                            isAuth.setValue(new Result.Error(new Exception("Exception not handled")));
                    }
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
