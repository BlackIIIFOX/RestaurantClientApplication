package com.tamagotchi.tamagotchiserverprotocol;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantClient {
    private static RestaurantClient instance = null;
    private Retrofit retrofit;
    private OkHttpClient client;
    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Miwicm9sZSI6Ik1hbmFnZXIiLCJpYXQiOjE1ODY5NzEyNTV9.lasbKegsStSGla3JY3dsIJLmQ2PriyGDh9kA8xA9Jds";

    private IAccountsApiService accountsService;

    private RestaurantClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        String BASE_URL = "http://192.168.56.1:3000/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        accountsService = retrofit.create(IAccountsApiService.class);
    }

    public synchronized static RestaurantClient getInstance() {
        if (instance == null) {
            instance = new RestaurantClient();
        }

        return instance;
    }

    public IAccountsApiService getAccountService() {
        return accountsService;
    }
}
