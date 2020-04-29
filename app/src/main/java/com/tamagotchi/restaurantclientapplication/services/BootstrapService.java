package com.tamagotchi.restaurantclientapplication.services;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.data.RestaurantsRepository;
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
                client.getAuthenticateInfoService()
        );

        AccountsRepository.InitializeService(client.getAccountService());
        RestaurantsRepository.InitializeService(client.getRestaurantsService());


        isInitialized = true;
    }
}
