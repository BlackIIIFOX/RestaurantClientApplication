package com.tamagotchi.tamagotchiserverprotocol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("role")
    @Expose
    private int role;

    @SerializedName("full_name")
    @Expose
    private String fullName;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public int getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
