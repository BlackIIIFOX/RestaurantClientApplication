package com.tamagotchi.restaurantclientapplication.services;

import com.tamagotchi.restaurantclientapplication.data.model.OrderVisitInfo;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.tamagotchiserverprotocol.models.MenuItem;
import com.tamagotchi.tamagotchiserverprotocol.models.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class OrderManager {

    private static volatile OrderManager instance;
    private static final Object syncInstance = new Object();

    public static OrderManager getInstance() {
        synchronized (syncInstance) {
            return instance;
        }
    }

    static void InitializeService(RestaurantsRepository restaurantsRepository) {
        synchronized (syncInstance) {
            instance = new OrderManager(restaurantsRepository);
        }
    }

    /**
     * Репозиторий ресторана для запроса ресторана по id.
     */
    private RestaurantsRepository restaurantsRepository;

    /**
     * Выбранный ресторан.
     */
    private BehaviorSubject<RestaurantModel> selectedRestaurantSubject;

    /**
     * Subject на выбранные элемены меню.
     */
    private BehaviorSubject<List<MenuItem>> orderMenuSubject;

    /**
     * Коллекция выбранных элементов меню.
     */
    private ArrayList<MenuItem> orderMenuArray = new ArrayList<>();

    /**
     * Subject информаци о посещении.
     */
    private BehaviorSubject<OrderVisitInfo> orderVisitInfoSubject;

    private OrderManager(RestaurantsRepository restaurantsRepository) {
        selectedRestaurantSubject = BehaviorSubject.create();
        orderMenuSubject = BehaviorSubject.create();
        orderVisitInfoSubject = BehaviorSubject.create();
    }

    /**
     * Возвращает выбранные пользователем ресторан.
     * @return observable на выбранный ресторан пользователем.
     */
    public Observable<RestaurantModel> selectedRestaurant() {
        return selectedRestaurantSubject.hide();
    }

    /**
     * Выбрать новый ресторан в качестве используемого.
     * После установки происходит запрос ресторана с сервера.
     * Получить сущность установленного ресторана можно подписавшись на selectedRestaurant.
     * @param restaurantId id ресторана, который выбран.
     */
    public void setSelectedRestaurant(int restaurantId) {

        restaurantsRepository.getRestaurantById(restaurantId).subscribe(restaurant -> {
            selectedRestaurantSubject.onNext(restaurant);
        }, error -> {
            selectedRestaurantSubject.onError(error);
        });
    }

    /**
     * Выбрать новый ресторан в качестве используемого.
     * Получить сущность установленного ресторана можно подписавшись на selectedRestaurant.
     * @param restaurant ресторан, который выбран.
     */
    public void setSelectedRestaurant(RestaurantModel restaurant) {
        selectedRestaurantSubject.onNext(restaurant);
    }

    /**
     * Возвращает элементы меню, которые выбирает пользователь.
     * При подписке происходит emit коллекции элементов.
     * @return observer на коллекцию выбранных элементов меню.
     */
    public Observable<List<MenuItem>> oderMenu() {
        return orderMenuSubject.hide();
    }

    /**
     * Добавить новый элемент (блюдо) меню в заказ.
     * @param menuItem элемент меню.
     */
    public void addToOrderMenu(MenuItem menuItem) {
        orderMenuArray.add(menuItem);
        orderMenuSubject.onNext(orderMenuArray);
    }

    /**
     * Удалить элемент меню (блюдо) из заказа.
     * @param menuItem удаляемый элемент.
     */
    public void removeFromOrderMenu(MenuItem menuItem) {
        orderMenuArray.remove(menuItem);
        orderMenuSubject.onNext(orderMenuArray);
    }

    /**
     * Очистить выбранное меню.
     */
    public void clearOrderMenu() {
        orderMenuArray.clear();
        orderMenuSubject.onNext(orderMenuArray);
    }

    /**
     * Установить информацию о посещении.
     * @param visitInfo информацию о посещении.
     */
    public void setOrderVisitInfo(OrderVisitInfo visitInfo) {
        orderVisitInfoSubject.onNext(visitInfo);
    }

    /**
     * Получить observable на информацию о посещении.
     * @return информация о посещении.
     */
    public Observable<OrderVisitInfo> getOrderVisitInfo() {
        return orderVisitInfoSubject.hide();
    }
}
