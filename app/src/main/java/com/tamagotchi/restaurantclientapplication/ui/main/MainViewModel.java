package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.restaurantclientapplication.services.OrderManager;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

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
     * Выбранный ресторан.
     */
    private MutableLiveData<RestaurantModel> selectedRestaurant  = new MutableLiveData<>();;

    MainViewModel(RestaurantsRepository restaurantsRepository) {
        this.restaurantsRepository = restaurantsRepository;
        InitRestaurants();
    }

    public LiveData<Result<List<RestaurantModel>>> getRestaurants() {
        return restaurants;
    }

    public LiveData<RestaurantModel> getSelectedRestaurant() { return selectedRestaurant; }

    public void setSelectedRestaurant(RestaurantModel restaurant) {
        selectedRestaurant.setValue(restaurant);
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
