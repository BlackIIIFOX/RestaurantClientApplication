package com.tamagotchi.tamagotchiserverprotocol.models;

public class AuthenticateInfoModel {
    private String jwt;

    private String message;

    public AuthenticateInfoModel(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }
}
