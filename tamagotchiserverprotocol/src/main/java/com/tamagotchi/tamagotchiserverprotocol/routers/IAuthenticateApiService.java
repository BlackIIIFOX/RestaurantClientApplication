package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.AccountInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * REST api from authenticate endpoint.
 */
public interface IAuthenticateApiService {
    /**
     * Authenticate user.
     * @param accountData account data with login and password.
     * @return jwt token.
     */
    @POST("authenticate")
    Call<AuthenticateInfoModel> authenticate(@Body SignInfoModel accountData);
}
