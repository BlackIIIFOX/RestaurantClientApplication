package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.DishModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDishesApiService {

    /**
     * Get all dishes.
     * @return dishes in system.
     */
    @GET("dishes")
    Single<List<DishModel>> getAllDishes();

    /**
     * Get dish by id.
     * @return dish.
     */
    @GET("dishes/{id}")
    Single<DishModel> getDishById(@Path("id") Integer id);

}
