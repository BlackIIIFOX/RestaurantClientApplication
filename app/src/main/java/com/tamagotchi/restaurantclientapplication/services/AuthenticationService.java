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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class AuthenticationService {

    private static final Object syncInstance = new Object();
    private static AuthenticationService instance;

    private MutableLiveData<Result> isAuth = new MutableLiveData<>();
    private IAuthenticateApiService authenticateApiService;
    private IAuthenticateInfoService authenticateInfoService;
    private AuthenticationInfoStorageService storageService = new AuthenticationInfoStorageService();

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

        this.authenticateApiService.authenticate(loginInfoModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            // Если сервер не вернул jwt, но запрос был успешен, то нажуно проверять сервер.
                            if (result.getToken().isEmpty()) {
                                isAuth.setValue(new Result.Error(new Exception("Server fault. Check the server.")));
                                return;
                            }

                            // Сохраяем jwt и авторизируемся
                            authenticateInfoService.LogIn(result);
                            storageService.saveToken(result.getToken());

                            Result next = new Result.Success<>(true);
                            isAuth.setValue(next);
                        },
                        error -> {
                            if (error instanceof HttpException) {
                                HttpException httpError = (HttpException) error;

                                switch (httpError.code()) {
                                    case 401:
                                        isAuth.setValue(new Result.Error(new AuthPasswordException()));
                                        break;
                                    case 404:
                                        isAuth.setValue(new Result.Error(new AuthLoginException()));
                                        break;
                                    default:
                                        isAuth.setValue(new Result.Error(new Exception("Exception not handled")));
                                }
                            } else {
                                isAuth.setValue(new Result.Error(new Exception(error)));
                            }
                        });

        return isAuth;
    }

    public void checkAuthenticate() {

        if (!authenticateInfoService.isAuthenticate()) {
            String token = storageService.getToken();
            if (!token.isEmpty()) {
                authenticateInfoService.LogIn(new AuthenticateInfoModel(token));
            }
        }

        if (authenticateInfoService.isAuthenticate()) {
            isAuth.setValue(new Result.Success());
        } else {
            isAuth.setValue(new Result.Error(new Exception()));
        }
    }


}
