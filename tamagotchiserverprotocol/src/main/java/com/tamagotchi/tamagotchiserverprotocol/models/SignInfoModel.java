package com.tamagotchi.tamagotchiserverprotocol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Данные отправляемые при запросе аутентификации и регистрации.
 */
public class SignInfoModel {
    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    public SignInfoModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
