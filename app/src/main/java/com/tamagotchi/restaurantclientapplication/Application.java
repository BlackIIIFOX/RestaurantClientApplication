package com.tamagotchi.restaurantclientapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Application extends android.app.Application {
    private SharedPreferences mPrefs;
    private static Application mApp;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //mPrefs.edit().remove("token").apply();
    }

    public static Application get() {
        return mApp;
    }

    public static SharedPreferences getPrefs() {
        return get().mPrefs;
    }
}
