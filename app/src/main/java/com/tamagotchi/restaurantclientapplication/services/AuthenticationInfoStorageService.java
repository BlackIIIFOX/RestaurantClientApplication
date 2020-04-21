package com.tamagotchi.restaurantclientapplication.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tamagotchi.restaurantclientapplication.Application;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;
import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;

public class AuthenticationInfoStorageService {
    private static final String TAG = "AuthenticationInfoStorageService";
    private final static String TOKEN = "token";

    public void loadToken() {
        String token = getToken();
        if (!token.isEmpty()) {
            RestaurantClient.getInstance().getAuthenticateInfoService().LogIn(new AuthenticateInfoModel(token));
        }
    }

    public String getToken() {
        String token = Application.getPrefs().getString(TOKEN, "");
        Log.i(TAG, "Token get: " + token);
        return token;
    }

    public void removeToken() {
        SharedPreferences.Editor edit;

        edit = Application.getPrefs().edit();
        edit.remove(TOKEN);
        Log.i(TAG, "Token remove");
        edit.apply();
    }

    public void saveToken(String token) {
        SharedPreferences.Editor edit;

        edit = Application.getPrefs().edit();
        edit.putString(TOKEN, token);
        Log.i(TAG, "Token save " + token);
        edit.apply();
    }
}
