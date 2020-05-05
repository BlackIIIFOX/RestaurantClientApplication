package com.tamagotchi.tamagotchiserverprotocol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tamagotchi.tamagotchiserverprotocol.models.enums.Roles;

public class UserModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("role")
    @Expose
    private Roles role;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("isBlocked")
    @Expose
    private String isBlocked;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Roles getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
