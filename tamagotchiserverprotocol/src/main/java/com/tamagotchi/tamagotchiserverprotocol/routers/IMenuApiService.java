package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.DishModel;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMenuApiService {
    /**
     * Get restaurant menu.
     * @return restaurant menu.
     */
    @GET("restaurants/{restaurantId}/menu")
    Single<List<MenuItem>> getMenu(@Path("restaurantId") Integer restaurantId);

    /**
     * Get menu item by id.
     * @return menu item.
     */
    @GET("restaurants/{restaurantId}/{menuItemId}")
    Single<MenuItem> getMenuItemById(@Path("restaurantId") Integer restaurantId, @Path("menuItemId") Integer menuItemId);
}
