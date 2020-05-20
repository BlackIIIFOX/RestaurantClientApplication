package com.tamagotchi.restaurantclientapplication.data.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tamagotchi.restaurantclientapplication.Application;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.tamagotchiserverprotocol.routers.IDishesApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IFilesApiService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.http.Url;

public class FilesRepository {
    private static volatile FilesRepository instance;

    // private IDishesApiService dishesApiService;
    private static final Object syncInstance = new Object();
    private IFilesApiService filesApiService;

    // private constructor : singleton access
    private FilesRepository(IFilesApiService filesApiService) {
        this.filesApiService = filesApiService;
    }

    public static FilesRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IFilesApiService filesApiService) {
        synchronized (syncInstance) {
            instance = new FilesRepository(filesApiService);
        }
    }

    public Single<Bitmap> getImageById(int id) {
        // TODO: добавить кеширование.
        return this.filesApiService.downloadFileWithDynamicUrlSync("https://restaurant-tamagotchi.ru:3000/api/files/" + id)
        .map(responseBody -> BitmapFactory.decodeStream(responseBody.byteStream()));
    }
}
