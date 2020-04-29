package com.tamagotchi.restaurantclientapplication.data;

import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.NotFoundException;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;
import com.tamagotchi.tamagotchiserverprotocol.routers.IRestaurantsApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
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
        ArrayList<RestaurantModel> array = new ArrayList<>();

        // mock restaurants.
        RestaurantModel restaurantGallery = new RestaurantModel();
        restaurantGallery.setAddress("Лиговский пр., 30 А, 5 этаж");
        restaurantGallery.setPositionLatitude(59.928200);
        restaurantGallery.setPositionLongitude(30.360137);
        restaurantGallery.setId(1);
        restaurantGallery.setCardPaymentPresent(true);
        restaurantGallery.setParkingPresent(false);
        restaurantGallery.setWifiPresent(true);
        restaurantGallery.setPhotos(Arrays.asList(1, 2, 3, 4, 5, 6));
        array.add(restaurantGallery);

        RestaurantModel restaurantRuby = new RestaurantModel();
        restaurantRuby.setAddress("Рубинштейна.");
        restaurantRuby.setPositionLatitude(59.932044);
        restaurantRuby.setPositionLongitude(30.346082);
        restaurantRuby.setId(2);
        restaurantRuby.setCardPaymentPresent(false);
        restaurantRuby.setParkingPresent(true);
        restaurantRuby.setWifiPresent(false);
        restaurantRuby.setPhotos(Arrays.asList(7, 9));
        array.add(restaurantRuby);

        return Single.fromObservable(Observable.fromArray(array));

        // TODO: венуть
        /*return Single.create(source ->
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
        );*/
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
