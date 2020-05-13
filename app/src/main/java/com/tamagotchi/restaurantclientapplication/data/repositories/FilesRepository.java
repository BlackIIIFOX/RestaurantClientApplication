package com.tamagotchi.restaurantclientapplication.data.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tamagotchi.restaurantclientapplication.Application;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.tamagotchiserverprotocol.routers.IDishesApiService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Url;

public class FilesRepository {
    private static volatile FilesRepository instance;

    // private IDishesApiService dishesApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private FilesRepository() {
    }

    public static FilesRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService() {
        synchronized (syncInstance) {
            instance = new FilesRepository();
        }
    }

    public Single<Bitmap> getImageById(int id) {
        try {
            URL url = new URL("http:/restaurant-tamagotchi.ru:3000/api/files/" + id);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return Single.just(bmp);
        } catch (Exception e) {
            return Single.error(new Exception(e.getMessage()));
        }
    }
}
