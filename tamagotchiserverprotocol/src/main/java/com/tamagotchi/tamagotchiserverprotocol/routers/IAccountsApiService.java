package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import java.util.List;

import retrofit2.Call;
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
    Call<List<AccountInfoModel>> getAccounts();

    @GET("accounts/{id}")
    Call<AccountInfoModel> getAccount(@Path("uid") int id);

    /**
     * Create user.
     * @param accountData Account data with login, password and role (if admin).
     * @return new user id.
     */
    @POST("accounts/")
    Call<AccountInfoModel> createAccount(@Body SignInfoModel accountData);
}
