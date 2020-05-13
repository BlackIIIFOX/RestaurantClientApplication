package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    /**
     * Репозиторий ресторанов.
     */
    private RestaurantsRepository restaurantsRepository;

    /**
     * Все ресторы в системе.
     */
    private MutableLiveData<Result<List<RestaurantModel>>> restaurants = new MutableLiveData<>();

    /**
     *  Информация о посещении выбранного ресторана.
     */
    private MutableLiveData<OrderVisitInfo> orderVisitInfo = new MutableLiveData<>();

    /**
     * Выбранный ресторан.
     */
    private MutableLiveData<RestaurantModel> selectedRestaurant  = new MutableLiveData<>();;

    MainViewModel(RestaurantsRepository restaurantsRepository) {
        this.restaurantsRepository = restaurantsRepository;
        InitRestaurants();
        InitOrderVisitInfo();
    }

    private void InitOrderVisitInfo() {
        Calendar visitTIme = Calendar.getInstance();
        visitTIme.add(Calendar.HOUR, 1);
        orderVisitInfo.setValue(new OrderVisitInfo(visitTIme, 1));
    }

    public LiveData<Result<List<RestaurantModel>>> getRestaurants() {
        return restaurants;
    }

    public LiveData<RestaurantModel> getSelectedRestaurant() { return selectedRestaurant; }

    public void setSelectedRestaurant(RestaurantModel restaurant) {
        selectedRestaurant.setValue(restaurant);
    }

    public LiveData<OrderVisitInfo> getOrderVisitInfo() {
        return orderVisitInfo;
    }

    public void setOrderVisitInfo(OrderVisitInfo visitInfo) {
        orderVisitInfo.setValue(visitInfo);
    }

    private void InitRestaurants() {
        this.restaurantsRepository.getAllRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        restaurants -> {
                            this.restaurants.setValue(new Result.Success(restaurants));
                        },
                        error -> {
                            this.restaurants.setValue(new Result.Error(new Exception(error)));
                        }
                );
    }
}
