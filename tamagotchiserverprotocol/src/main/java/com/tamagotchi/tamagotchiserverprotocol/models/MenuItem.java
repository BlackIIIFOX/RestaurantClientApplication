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

    public MenuItem(int id, int price, int dishId, boolean isDeleted) {
        this.id = id;
        this.price = price;
        this.dishId = dishId;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getDishId() {
        return dishId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
