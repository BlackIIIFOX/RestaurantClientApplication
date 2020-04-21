package com.tamagotchi.restaurantclientapplication.services;

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

        AuthenticationService.InitializeService(
                RestaurantClient.getInstance().getAuthenticateService(),
                RestaurantClient.getInstance().getAuthenticateInfoService()
        );

        isInitialized = true;
    }
}
