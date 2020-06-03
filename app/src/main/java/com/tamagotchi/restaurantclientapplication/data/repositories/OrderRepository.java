package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderCreateModel;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderModel;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderPathModel;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;
import com.tamagotchi.tamagotchiserverprotocol.models.enums.OrderStatus;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAuthenticateApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IOrdersApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.http.Query;

public class OrderRepository {
    private static volatile OrderRepository instance;

    private IOrdersApiService ordersApiService;
    private AuthenticationService authenticateApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private OrderRepository(IOrdersApiService ordersApiService, AuthenticationService authenticateApiService) {
        this.ordersApiService = ordersApiService;
        this.authenticateApiService = authenticateApiService;
    }

    public static OrderRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IOrdersApiService ordersApiService, AuthenticationService authenticationService) {
        synchronized (syncInstance) {
            instance = new OrderRepository(ordersApiService, authenticationService);
        }
    }

    /**
     * Получить все заказы пользователя.
     *
     * @return Single на коллекци заказов
     */
    public Single<List<OrderModel>> getUserOrders(int userId) {
        return ordersApiService.getAllOrders(userId, null, null, null);
    }

    public Single<OrderModel> getOrderById(Integer orderId) {
        return null;
    }

    public Completable createOrder(OrderCreateModel newOder) {
        return ordersApiService.createOrder(newOder);
    }

    public Single<OrderModel> pathOrder(Integer orderId, OrderPathModel order) {
        return null;
    }
}
