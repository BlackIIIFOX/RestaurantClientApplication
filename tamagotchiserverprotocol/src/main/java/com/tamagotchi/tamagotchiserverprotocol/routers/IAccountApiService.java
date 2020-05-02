package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.POST;

public interface IAccountApiService {
    /**
     * Get information about account by token.
     * @return Account information.
     */
    @POST("account")
    Single<UserModel> getCurrentAccount();
}
