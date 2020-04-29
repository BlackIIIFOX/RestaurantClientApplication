package com.tamagotchi.restaurantclientapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AccountExistException;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAccountsApiService;
import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class AccountsRepository {

    private static volatile AccountsRepository instance;

    private IAccountsApiService accountsApiService;
    private AccountsDAO accountsDAO;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private AccountsRepository(IAccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    public static AccountsRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IAccountsApiService accountsApiService) {
        synchronized (syncInstance) {
            instance = new AccountsRepository(accountsApiService);
        }
    }

    public Completable createAccount(LoginInfo loginInfo) {

        if (loginInfo.getLogin().isEmpty() ||
                loginInfo.getPassword() == null || loginInfo.getPassword().getPasswordMd5().isEmpty()) {
            throw new IllegalArgumentException("No login or password");
        }

        SignInfoModel signUpInfo = new SignInfoModel(loginInfo.getLogin(),
                loginInfo.getPassword().getPasswordMd5());

        return Completable.create(source -> {
            accountsApiService.createAccount(signUpInfo)
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

    private LiveData<AccountInfoModel> getCurrantAccount() {
        return null;
    }

}
