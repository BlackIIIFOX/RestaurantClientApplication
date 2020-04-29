package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IRestaurantsApiService {
    /**
     * Get all restaurants.
     * @return List of restaurants.
     */
    @GET("restaurants")
    Single<List<RestaurantModel>> getAllRestaurants();

    /**
     * Get restaurant by id.
     * @return info about restaurant.
     */
    @GET("restaurants/{id}")
    Single<RestaurantModel> getRestaurantById(@Path("id") Integer id);
}
