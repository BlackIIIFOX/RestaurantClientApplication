package com.tamagotchi.tamagotchiserverprotocol.models.enums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

public class UserWithJwtModel extends UserModel {
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
