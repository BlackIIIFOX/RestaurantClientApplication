package com.tamagotchi.restaurantclientapplication.data.repositories;

import com.tamagotchi.tamagotchiserverprotocol.models.OrderCreateModel;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderModel;
import com.tamagotchi.tamagotchiserverprotocol.models.OrderPathModel;
import com.tamagotchi.tamagotchiserverprotocol.models.enums.OrderStatus;
import com.tamagotchi.tamagotchiserverprotocol.routers.IOrdersApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderRepository {
    private static volatile OrderRepository instance;

    private IOrdersApiService ordersApiService;
    private static final Object syncInstance = new Object();

    // private constructor : singleton access
    private OrderRepository(IOrdersApiService ordersApiService) {
        this.ordersApiService = ordersApiService;
    }

    public static OrderRepository getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    public static void InitializeService(IOrdersApiService ordersApiService) {
        synchronized (syncInstance) {
            instance = new OrderRepository(ordersApiService);
        }
    }

    public Single<List<OrderModel>> getAllOrders(String clientFilter, OrderStatus statusFilter, OrderStatus cooksStatusFilter, OrderStatus waitersStatusFilter) {
        return null;
    }

    public Single<OrderModel> getOrderById(Integer orderId) {
        return null;
    }

    public Completable createOrder(OrderCreateModel newOder) {
        return Completable.create(source -> {
            ordersApiService.createOrder(newOder)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(source::onComplete, error -> {
                        source.onError(new Exception(error));
                    });
        });
    }

    public Single<OrderModel> pathOrder(Integer orderId, OrderPathModel order) {
        return null;
    }
}
