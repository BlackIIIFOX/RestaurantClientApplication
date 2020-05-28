package com.tamagotchi.restaurantclientapplication.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.tamagotchi.restaurantclientapplication.Application;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;
import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;

public class AuthenticationInfoStorageService {
    private static final String TAG = "AuthenticationInfoStorageService";
    private final static String TOKEN = "token";

    String getToken() {
        String token = Application.getPrefs().getString(TOKEN, "");
        Log.i(TAG, "Token get: " + token);
        return token;
    }

    void removeToken() {
        SharedPreferences.Editor edit;

        edit = Application.getPrefs().edit();
        edit.remove(TOKEN);
        Log.i(TAG, "Token remove");
        edit.apply();
    }

    void saveToken(String token) {
        SharedPreferences.Editor edit;

        edit = Application.getPrefs().edit();
        edit.putString(TOKEN, token);
        Log.i(TAG, "Token save " + token);
        edit.apply();
    }
}
