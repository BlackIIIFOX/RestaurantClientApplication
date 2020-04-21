package com.tamagotchi.tamagotchiserverprotocol.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;

/**
 * Сервис, отвечающий за аутентификацию пользователя.
 */
public class AuthenticateInfoService implements IAuthenticateInfoService {
    private AuthenticateInfoModel authentication;

    public boolean isAuthenticate() {
        return authentication != null;
    }

    public void LogIn(AuthenticateInfoModel authentication) {
        this.authentication = authentication;
    }

    public void LogOut() {
        authentication = null;
    }

    @Override
    public AuthenticateInfoModel getAuthenticateInfo() {
        return authentication;
    }
}
