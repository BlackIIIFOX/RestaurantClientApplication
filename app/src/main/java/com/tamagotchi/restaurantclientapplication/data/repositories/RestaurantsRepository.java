package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.NotFoundException;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IRestaurantsApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class RestaurantsRepository {
    private static volatile RestaurantsRepository instance;
    private static final Object syncInstance = new Object();
    private IRestaurantsApiService restaurantsApiService;

    // private constructor : singleton access
    private RestaurantsRepository(IRestaurantsApiService restaurantsApiService) {
        this.restaurantsApiService = restaurantsApiService;
    }

    public synchronized static void InitializeService(IRestaurantsApiService restaurantsApiService) {
        synchronized (syncInstance) {
            instance = new RestaurantsRepository(restaurantsApiService);
        }
    }

    public static RestaurantsRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    /**
     * Получить все рестораны в системе.
     *
     * @return Наблюдаемая коллекция ресторанов.
     */
    public Single<List<RestaurantModel>> getAllRestaurants() {
        return Single.create(source ->
                this.restaurantsApiService.getAllRestaurants()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    source.onSuccess(result);
                                },
                                error -> {
                                    if (error instanceof HttpException) {
                                        HttpException httpError = (HttpException) error;

                                        if (httpError.code() == 401) {
                                            source.onError(new AuthPasswordException());
                                        } else {
                                            source.onError(new Exception(error));
                                        }
                                    } else {
                                        source.onError(new Exception(error));
                                    }
                                }
                        )
        );
    }

    public Single<RestaurantModel> getRestaurantById(Integer id) {
        return Single.create(source ->
                this.restaurantsApiService.getRestaurantById(id)
                        .subscribe(
                                source::onSuccess,
                                error -> {
                                    if (error instanceof HttpException) {
                                        HttpException httpError = (HttpException) error;

                                        switch (httpError.code()) {
                                            case 401:
                                                source.onError(new AuthPasswordException());
                                                break;
                                            case 404:
                                                source.onError(new NotFoundException());
                                                break;
                                            default:
                                                source.onError(new Exception(error));
                                        }
                                    } else {
                                        source.onError(new Exception(error));
                                    }
                                }
                        )
        );
    }
}