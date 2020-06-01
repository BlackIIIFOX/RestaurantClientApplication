package com.tamagotchi.restaurantclientapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.restaurantclientapplication.services.BootstrapService;
import com.tamagotchi.restaurantclientapplication.ui.start.StartActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Application extends android.app.Application {
    private SharedPreferences mPrefs;
    private static Application mApp;
    private boolean workInMainViewModel;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        BootstrapService.getInstance().InitializeApplication();

        InitLogoutHandler();
    }

    public static Application get() {
        return mApp;
    }

    private void InitLogoutHandler() {
        AuthenticationService.getInstance().isAuthenticated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isAuth -> {
                    // Если пользователь был разлогирован,
                    // то нужно закрыть доступ к основной части приложения.
                    if (!isAuth && workInMainViewModel) {
                        Intent loginActivity = new Intent(this, StartActivity.class);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginActivity);
                    }
                });
    }

    public static void startWorking() {
        get().workInMainViewModel = true;
    }

    public static SharedPreferences getPrefs() {
        return get().mPrefs;
    }
}
