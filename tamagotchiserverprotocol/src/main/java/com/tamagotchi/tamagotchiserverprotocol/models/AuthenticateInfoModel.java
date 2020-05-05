package com.tamagotchi.tamagotchiserverprotocol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticateInfoModel {
    @SerializedName("token")
    @Expose
    private String token;

    public AuthenticateInfoModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
