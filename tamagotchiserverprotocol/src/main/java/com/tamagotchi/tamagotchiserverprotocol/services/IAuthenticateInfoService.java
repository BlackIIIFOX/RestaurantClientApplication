package com.tamagotchi.tamagotchiserverprotocol.services;

import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;

public interface IAuthenticateInfoService {

    /**
     * Проверяет выполнена ли авторизация.
     * @return true - выполнена.
     */
    boolean isAuthenticate();

    /**
     * Установливает данные для аутентификации.
     * @param authentication данные аутентификаци.
     */
    void LogIn(AuthenticateInfoModel authentication);

    /**
     * Прекращает использовать данные аутентификации.
     */
    void LogOut();

    /**
     * Возвращает данные для аутентификации.
     * @return данные аутентификации.
     */
    AuthenticateInfoModel getAuthenticateInfo();
}
