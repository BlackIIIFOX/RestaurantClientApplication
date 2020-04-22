package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.AuthenticateInfoModel;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import io.reactivex.rxjava3.core.Single;
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
    Single<AuthenticateInfoModel> authenticate(@Body SignInfoModel accountData);
}
