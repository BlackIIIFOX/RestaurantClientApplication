package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.NotFoundException;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IMenuApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MenuRepository {
    private static volatile MenuRepository instance;

    private IMenuApiService menuApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private MenuRepository(IMenuApiService menuApiService) {
        this.menuApiService = menuApiService;
    }

    public static MenuRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IMenuApiService menuApiService) {
        synchronized (syncInstance) {
            instance = new MenuRepository(menuApiService);
        }
    }

    /**
     * Получить меню конкретного ресторана.
     * @param restaurantId идентификатор ресторана.
     * @return Меню ресторана.
     */
    public Single<List<MenuItem>> getMenu(int restaurantId) {
        return Single.create(source ->
                this.menuApiService.getMenu(restaurantId)
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
     * Получить содержимое меню рестоарана по id.
     * @param restaurantId id ресторана, в котором находитс меню.
     * @param menuItemId id элемента в меню.
     * @return элемент меню.
     */
    public Single<MenuItem> getMenuItemById(int restaurantId, int menuItemId) {
        return Single.create(source ->
                this.menuApiService.getMenuItemById(restaurantId, menuItemId)
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
