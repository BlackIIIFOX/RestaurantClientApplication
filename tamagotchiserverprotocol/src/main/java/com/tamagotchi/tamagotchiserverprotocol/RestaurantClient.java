package com.tamagotchi.tamagotchiserverprotocol;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import java.util.concurrent.TimeUnit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantClient {
    private static RestaurantClient instance = null;
    private Retrofit retrofit;
    private OkHttpClient client;

    private IRestaurantApiService restaurantApiService;

    private RestaurantClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

        String BASE_URL = "http://192.168.56.1:3000/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        restaurantApiService = retrofit.create(IRestaurantApiService.class);
    }

    public synchronized static RestaurantClient getInstance() {
        if (instance == null) {
            instance = new RestaurantClient();
        }

        return instance;
    }

    public IRestaurantApiService getAccountService() {
        return restaurantApiService;
    }
}
