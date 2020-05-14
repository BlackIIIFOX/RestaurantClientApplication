package com.tamagotchi.tamagotchiserverprotocol;

import android.annotation.SuppressLint;

import com.tamagotchi.tamagotchiserverprotocol.routers.IAccountApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IDishesApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IMenuApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IUsersApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IAuthenticateApiService;
import com.tamagotchi.tamagotchiserverprotocol.routers.IRestaurantsApiService;
import com.tamagotchi.tamagotchiserverprotocol.services.AuthenticateInfoService;
import com.tamagotchi.tamagotchiserverprotocol.services.IAuthenticateInfoService;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс для работы с сервером. Является singleton.
 */
public class RestaurantClient {
    private static RestaurantClient instance = null;
    public static final String  BASE_URL = "https://restaurant-tamagotchi.ru:3000/api/";

    private final IUsersApiService usersServices;
    private final AuthenticateInfoService authenticateInfoService = new AuthenticateInfoService();
    private final IAuthenticateApiService authenticateService;
    private final IRestaurantsApiService restaurantsService;
    private final IAccountApiService accountService;
    private final IDishesApiService dishesService;
    private final IMenuApiService menuApiService;

    /**
     * Инициализация retrofit клиента.
     */
    private RestaurantClient() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = generateDefaultOkHttpBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    // Добавляем JWT токен в запрос для аутентификации.
                    Request.Builder newRequest = chain.request().newBuilder();
                    if (authenticateInfoService.isAuthenticate()) {
                        newRequest.addHeader("Authorization", "Bearer " + authenticateInfoService.getAuthenticateInfo().getToken());
                    }

                    return chain.proceed(newRequest.build());
                })
                .addInterceptor(logger)
                .build();

        // Собираем retrofit клиент для отпрвки запросов на сервер.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();

        // Инициализируем маршрутизаторы retrofit
        usersServices = retrofit.create(IUsersApiService.class);
        authenticateService = retrofit.create(IAuthenticateApiService.class);
        restaurantsService = retrofit.create(IRestaurantsApiService.class);
        accountService = retrofit.create(IAccountApiService.class);
        dishesService = retrofit.create(IDishesApiService.class);
        menuApiService = retrofit.create(IMenuApiService.class);
    }

    private static OkHttpClient.Builder generateDefaultOkHttpBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder;
    }

    public synchronized static RestaurantClient getInstance() {
        if (instance == null) {
            instance = new RestaurantClient();
        }

        return instance;
    }

    /**
     * Предоставляет сервис для работы с пользователями системы.
     *
     * @return /api/user service
     */
    public IUsersApiService getUsersService() {
        return usersServices;
    }

    /**
     * Предоставляет сервис для работы с авторизацией системы.
     *
     * @return /api/authenticate service
     */
    public IAuthenticateApiService getAuthenticateService() {
        return authenticateService;
    }

    /**
     * Предоставляет сервис для работы с данными авторизации.
     * Большенство запросов не будут работать без их установки.
     * Для получения данных аутентификации используйте {@linkplain IUsersApiService
     * AccountsApiService}
     *
     * @return /api/user service
     */
    public IAuthenticateInfoService getAuthenticateInfoService() {
        return authenticateInfoService;
    }

    /**
     * Предоставляет сервис для работы с ресторанми.
     * @return /api/restaurants service
     */
    public IRestaurantsApiService getRestaurantsService() { return restaurantsService; }

    /**
     * Предоставлят сервис для работы с аккаунтом пользователя.
     * @return /api/account
     */
    public IAccountApiService getAccountService() { return accountService; }

    /**
     * Предоставлят сервис для работы с блюдами в системе.
     * @return /api/dishes
     */
    public IDishesApiService getDishesService() { return dishesService; }

    /**
     * Предоставлят сервис для работы с меню ресторана.
     * @return /api/dishes
     */
    public IMenuApiService getMenuService() { return menuApiService; }
}
