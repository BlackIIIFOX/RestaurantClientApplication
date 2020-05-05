package com.tamagotchi.restaurantclientapplication.ui.restaurants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RestaurantsViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private RestaurantsRepository restaurantsRepository;
    private MutableLiveData<Result<List<RestaurantModel>>> restaurants = new MutableLiveData<>();

    public RestaurantsViewModel() {
        this.restaurantsRepository = RestaurantsRepository.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is restaurants fragment");

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

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Result<List<RestaurantModel>>> getRestaurants() { return restaurants; };
}
