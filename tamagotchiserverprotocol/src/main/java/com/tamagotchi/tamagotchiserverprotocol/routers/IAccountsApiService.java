package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * REST api from accounts endpoint.
 */
public interface IAccountsApiService {

    /**
     * Get all accounts from server.
     * @return collections accounts.
     */
    @GET("accounts/")
    Single<List<AccountInfoModel>> getAccounts();

    @GET("accounts/{id}")
    Single<AccountInfoModel> getAccount(@Path("uid") int id);

    /**
     * Create user.
     * @param accountData Account data with login, password and role (if admin).
     * @return new user id.
     */
    @POST("accounts/")
    Completable createAccount(@Body SignInfoModel accountData);
}
