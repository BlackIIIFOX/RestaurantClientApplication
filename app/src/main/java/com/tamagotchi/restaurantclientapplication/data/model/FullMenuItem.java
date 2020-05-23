package com.tamagotchi.restaurantclientapplication.data.model;

import com.tamagotchi.tamagotchiserverprotocol.models.DishModel;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;

/**
 * Определяем полную сущность элемента меню (информация о меню и информация о блюде)
 */
public class FullMenuItem {
    private int id;

    private int price;

    private boolean isDeleted;

    private DishModel dish;

    public FullMenuItem(int id, int price, boolean isDeleted, DishModel dish) {
        this.id = id;
        this.price = price;
        this.isDeleted = isDeleted;
        this.dish = dish;
    }

    public FullMenuItem(MenuItem menuItem, DishModel dish) {
        this.id = menuItem.getId();
        this.price = menuItem.getPrice();
        this.isDeleted = menuItem.isDeleted();
        this.dish = dish;
    }


    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public DishModel getDish() {
        return dish;
    }
}
