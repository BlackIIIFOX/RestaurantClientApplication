package com.tamagotchi.restaurantclientapplication.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.FullMenuItem;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.data.repositories.DishesRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.MenuRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.tamagotchiserverprotocol.models.DishModel;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private static final String LogTag = "MainViewModel";

    /**
     * Репозиторий ресторанов.
     */
    private RestaurantsRepository restaurantsRepository;

    /**
     * Репозиторий меню.
     */
    private MenuRepository menuRepository;

    /**
     * Репозиторий для блюд.
     */
    private DishesRepository dishesRepository;

    /**
     * Выбранный элемент навигации приложения (нижняя панель)
     */
    private MutableLiveData<Navigation> selectedNavigation = new MutableLiveData<>(Navigation.Restaurant);

    /**
     * Все ресторы в системе.
     */
    private MutableLiveData<Result<List<RestaurantModel>>> restaurants = new MutableLiveData<>();

    /**
     * Информация о посещении выбранного ресторана.
     */
    private MutableLiveData<OrderVisitInfo> orderVisitInfo = new MutableLiveData<>();

    /**
     * Выбранный ресторан.
     */
    private MutableLiveData<RestaurantModel> selectedRestaurant = new MutableLiveData<>();

    /**
     * Меню выбранного ресторана.
     */
    private MutableLiveData<Result<List<FullMenuItem>>> selectedRestaurantMenu = new MutableLiveData<>();

    private Disposable menuItemRequest = null;

    MainViewModel(RestaurantsRepository restaurantsRepository, DishesRepository dishesRepository, MenuRepository menuRepository) {
        this.restaurantsRepository = restaurantsRepository;
        this.dishesRepository = dishesRepository;
        this.menuRepository = menuRepository;
        InitRestaurants();
        InitOrderVisitInfo();
    }

    private void InitOrderVisitInfo() {
        Calendar visitTime = Calendar.getInstance();
        visitTime.add(Calendar.HOUR, 1);
        orderVisitInfo.setValue(new OrderVisitInfo(visitTime, 1));
    }

    public LiveData<Result<List<RestaurantModel>>> getRestaurants() {
        return restaurants;
    }

    public LiveData<RestaurantModel> getSelectedRestaurant() {
        return selectedRestaurant;
    }

    public void setSelectedRestaurant(RestaurantModel restaurant) {
        selectedRestaurant.setValue(restaurant);
        InitRestaurantMenu(restaurant);
    }

    public LiveData<Result<List<FullMenuItem>>> getSelectedRestaurantMenu() {
        return selectedRestaurantMenu;
    }

    public void setSelectedRestaurantMenu(Result<List<FullMenuItem>> restaurantMenu) {
        selectedRestaurantMenu.setValue(restaurantMenu);
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

    /**
     * Выполняем иницилизацию меню выбранного ресторана, для этого:
     * 1. Получаем коллекцию MenuItem с сервера.
     * 2. По каждому MenuItem запрашивем Dish, который ему принадлежит.
     * 3. Формируем FullMenuItem из Dish и MenuItem, делаем коллекцию из элементов и
     *  отправляем в LiveData.
     *  TODO: возможно стоит убрать возврат коллекций из репозиториев, но это довольно сложно.
     * @param restaurant ресторан, меню которого требуется инициализировать.
     */
    private void InitRestaurantMenu(RestaurantModel restaurant) {
        // Отписываемся от предыдущей подписка. Она нам больше не нужна, если ресторан был сменен.
        if (menuItemRequest != null) {
            menuItemRequest.dispose();
        }

        menuItemRequest = this.menuRepository.getMenu(restaurant.getId())
                .toObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(
                        list -> Observable.fromIterable(list)
                                .flatMap(
                                        menuItem -> this.dishesRepository.getDishById(menuItem.getDishId()).toObservable()
                                                .subscribeOn(Schedulers.io())
                                                .onErrorResumeNext(
                                                        x -> {
                                                            Log.e(LogTag, x.toString());
                                                            return Observable.empty();
                                                        })
                                                .map(
                                                        dishModel ->
                                                                new FullMenuItem(menuItem, dishModel)
                                                )))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        menuItems -> {
                            this.setSelectedRestaurantMenu(new Result.Success(menuItems));
                        }, error -> {
                            this.setSelectedRestaurantMenu(new Result.Error(new Exception(error)));
                        });
    }

    /**
     * Установить текущую навигацию приложения.
     *
     * @param navigation выбранный элемент меню.
     */
    public void setSelectedNavigation(Navigation navigation) {
        selectedNavigation.setValue(navigation);
    }

    /**
     * Возвращает observable на выбранный пользователем элемент навигации.
     * Используется для обработки переключения навигации.
     *
     * @return observable
     */
    public LiveData<Navigation> getSelectedNavigation() {
        return selectedNavigation;
    }
}
