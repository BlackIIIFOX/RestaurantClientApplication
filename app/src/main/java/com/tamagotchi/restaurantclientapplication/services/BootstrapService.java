package com.tamagotchi.restaurantclientapplication.services;

import com.tamagotchi.restaurantclientapplication.data.repositories.DishesRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.MenuRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.UsersRepository;
import com.tamagotchi.restaurantclientapplication.data.repositories.RestaurantsRepository;
import com.tamagotchi.tamagotchiserverprotocol.RestaurantClient;

public class BootstrapService {

    private static BootstrapService instance;
    private static boolean isInitialized = false;

    private BootstrapService() {

    }

    public synchronized static BootstrapService getInstance() {
        if (instance == null) {
            instance = new BootstrapService();
        }

        return instance;
    }

    /**
     * Инициалиирует компоненты приложения.
     */
    public synchronized void InitializeApplication() {
        if (isInitialized)
            return;

        RestaurantClient client = RestaurantClient.getInstance();

        AuthenticationService.InitializeService(
                client.getAuthenticateService(),
                client.getAuthenticateInfoService(),
                client.getAccountService()
        );

        UsersRepository.InitializeService(client.getUsersService());
        RestaurantsRepository.InitializeService(client.getRestaurantsService());
        DishesRepository.InitializeService(client.getDishesService());
        MenuRepository.InitializeService(client.getMenuService());

        isInitialized = true;
    }
}
