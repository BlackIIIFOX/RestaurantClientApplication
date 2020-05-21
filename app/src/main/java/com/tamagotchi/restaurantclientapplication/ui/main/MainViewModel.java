package com.tamagotchi.restaurantclientapplication.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.data.repositories.MenuRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    /**
     * Репозиторий ресторанов.
     */
    private RestaurantsRepository restaurantsRepository;

    /**
     * Репозиторий меню.
     */
    private MenuRepository menuRepository;

    /**
     * Выбранный элемент навигации приложения (нижняя панель)
     */
    private MutableLiveData<Navigation> selectedNavigation = new MutableLiveData<>(Navigation.Restaurant);

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
    private MutableLiveData<RestaurantModel> selectedRestaurant  = new MutableLiveData<>();

    /**
     * Меню выбранного ресторана.
     */
    private MutableLiveData<Result<List<MenuItem>>> selectedRestaurantMenu  = new MutableLiveData<>();

    MainViewModel(RestaurantsRepository restaurantsRepository) {
        this.restaurantsRepository = restaurantsRepository;
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

    public LiveData<RestaurantModel> getSelectedRestaurant() { return selectedRestaurant; }

    public void setSelectedRestaurant(RestaurantModel restaurant) {
        selectedRestaurant.setValue(restaurant);
        InitRestaurantMenu(restaurant);
    }

    public LiveData<Result<List<MenuItem>>> getSelectedRestaurantMenu() { return selectedRestaurantMenu; }

    public void setSelectedRestaurantMenu(Result<List<MenuItem>> restaurantMenu) {
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

    private void InitRestaurantMenu(RestaurantModel restaurant) {
        menuRepository = MenuRepository.getInstance();
        this.menuRepository.getMenu(restaurant.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        menuItems -> {
                            this.selectedRestaurantMenu.setValue(new Result.Success(menuItems));
                        },
                        error -> {
                            this.selectedRestaurantMenu.setValue(new Result.Error(new Exception(error)));
                        }
                );
    }

    /**
     * Установить текущую навигацию приложения.
     * @param navigation выбранный элемент меню.
     */
    public void setSelectedNavigation(Navigation navigation) {
        selectedNavigation.setValue(navigation);
    }

    /**
     * Возвращает observable на выбранный пользователем элемент навигации.
     * Используется для обработки переключения навигации.
     * @return observable
     */
    public LiveData<Navigation> getSelectedNavigation() {
        return selectedNavigation;
    }
}
