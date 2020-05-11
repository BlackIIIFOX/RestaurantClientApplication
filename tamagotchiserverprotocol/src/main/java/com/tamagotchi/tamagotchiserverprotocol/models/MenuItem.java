package com.tamagotchi.tamagotchiserverprotocol.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuItem {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("dish")
    @Expose
    private int dishId;

    @SerializedName("isDeleted")
    @Expose
    private boolean isDeleted;
}
