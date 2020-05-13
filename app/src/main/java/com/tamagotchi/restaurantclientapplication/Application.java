package com.tamagotchi.restaurantclientapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.restaurantclientapplication.services.BootstrapService;

public class Application extends android.app.Application {
    private SharedPreferences mPrefs;
    private static Application mApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        BootstrapService.getInstance().InitializeApplication();
        // AuthenticationService.getInstance().signOut();
    }

    public static Application get() {
        return mApp;
    }

    public static SharedPreferences getPrefs() {
        return get().mPrefs;
    }
}
