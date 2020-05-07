package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface IAccountApiService {
    /**
     * Get information about account by token.
     * @return Account information.
     */
    @GET("account")
    Single<UserModel> getCurrentAccount();
}
