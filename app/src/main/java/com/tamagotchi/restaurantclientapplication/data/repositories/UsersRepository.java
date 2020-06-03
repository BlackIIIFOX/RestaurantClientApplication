package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AccountExistException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.tamagotchiserverprotocol.models.UpdatableInfoUser;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IUsersApiService;
import com.tamagotchi.tamagotchiserverprotocol.models.CredentialsModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class UsersRepository {

    private static volatile UsersRepository instance;

    private IUsersApiService usersApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private UsersRepository(IUsersApiService accountsApiService) {
        this.usersApiService = accountsApiService;
    }

    public static UsersRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IUsersApiService accountsApiService) {
        synchronized (syncInstance) {
            instance = new UsersRepository(accountsApiService);
        }
    }

    public Completable createAccount(LoginInfo loginInfo) {

        if (loginInfo.getLogin().isEmpty() ||
                loginInfo.getPassword() == null || loginInfo.getPassword().getPasswordMd5().isEmpty()) {
            throw new IllegalArgumentException("No login or password");
        }

        CredentialsModel signUpInfo = new CredentialsModel(loginInfo.getLogin(),
                loginInfo.getPassword().getPasswordMd5());

        return Completable.create(source -> {
            usersApiService.createUser(signUpInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(source::onComplete, error -> {
                        if (error instanceof HttpException) {
                            HttpException httpError = (HttpException) error;

                            if (httpError.code() == 403) {
                                source.onError(new AccountExistException());
                            } else {
                                source.onError(new Exception(error));
                            }
                        } else {
                            source.onError(new Exception(error));
                        }
                    });
        });
    }

    public Single<List<UserModel>> getUsers() {
        return Single.create(source ->
                this.usersApiService.getUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                source::onSuccess,
                                error -> {
                                    if (error instanceof HttpException) {
                                        HttpException httpError = (HttpException) error;

                                        if (httpError.code() == 401) {
                                            source.onError(new AuthPasswordException());
                                        } else {
                                            source.onError(new Exception(error));
                                        }
                                    } else {
                                        source.onError(new Exception(error));
                                    }
                                }
                        )
        );
    }

    public Single<UserModel> getUserById(int id) {
        return Single.create(source ->
                this.usersApiService.getUserById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                source::onSuccess,
                                error -> {
                                    if (error instanceof HttpException) {
                                        HttpException httpError = (HttpException) error;

                                        if (httpError.code() == 401) {
                                            source.onError(new AuthPasswordException());
                                        } else {
                                            source.onError(new Exception(error));
                                        }
                                    } else {
                                        source.onError(new Exception(error));
                                    }
                                }
                        )
        );
    }

    public Single<UserModel> updateUser(UpdatableInfoUser update) {
        return Single.create(source ->
                this.usersApiService.updateUser(update)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                source::onSuccess,
                                error -> {
                                    if (error instanceof HttpException) {
                                        HttpException httpError = (HttpException) error;

                                        if (httpError.code() == 401) {
                                            source.onError(new AuthPasswordException());
                                        } else {
                                            source.onError(new Exception(error));
                                        }
                                    } else {
                                        source.onError(new Exception(error));
                                    }
                                }
                        )
        );
    }
}
