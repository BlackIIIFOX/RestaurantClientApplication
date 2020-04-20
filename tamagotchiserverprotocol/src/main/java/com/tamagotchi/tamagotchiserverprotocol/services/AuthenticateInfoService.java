package com.tamagotchi.tamagotchiserverprotocol.services;

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

}
