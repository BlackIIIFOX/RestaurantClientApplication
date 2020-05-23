package com.tamagotchi.restaurantclientapplication.services;

import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;

public class UrlConverterService {
    public static String IdToUrlConvert(int id) {
        return RestaurantClient.getInstance().getServerUrl() + "files/" + id;
    }
}
