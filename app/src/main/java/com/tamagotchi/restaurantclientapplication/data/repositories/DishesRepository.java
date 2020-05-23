package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.NotFoundException;
import com.tamagotchi.tamagotchiserverprotocol.models.DishModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IDishesApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class DishesRepository {
    private static volatile DishesRepository instance;

    private IDishesApiService dishesApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private DishesRepository(IDishesApiService dishesApiService) {
        this.dishesApiService = dishesApiService;
    }

    public static DishesRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IDishesApiService dishesApiService) {
        synchronized (syncInstance) {
            instance = new DishesRepository(dishesApiService);
        }
    }

    /**
     * Получить блюда всех ресторанов.
     * @return коллекция блюд.
     */
    public Single<List<DishModel>> getAllDishes() {
        return Single.create(source ->
                this.dishesApiService.getAllDishes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                source::onSuccess,
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

    /**
     * Получить блюдо по id.
     * @param id id блюда.
     * @return экземпляр блюда.
     */
    public Single<DishModel> getDishById(int id) {
        return Single.create(source ->
                this.dishesApiService.getDishById(id)
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
