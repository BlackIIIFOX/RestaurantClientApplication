package com.tamagotchi.tamagotchiserverprotocol.routers;

import com.tamagotchi.tamagotchiserverprotocol.models.UpdatableInfoUser;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;
import com.tamagotchi.tamagotchiserverprotocol.models.CredentialsModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * REST api from accounts endpoint.
 */
public interface IUsersApiService {

    /**
     * Get all users from server.
     * @return collections users.
     */
    @GET("users/")
    Single<List<UserModel>> getUsers();

    /**
     * Return user data by id.
     * @param id user id.
     * @return User info.
     */
    @GET("users/{id}")
    Single<UserModel> getUserById(@Path("uid") int id);

    /**
     * Update user.
     * @param update info for update.
     * @return updated user.
     */
    @PUT("users/")
    Single<UserModel> updateUser(@Body UpdatableInfoUser update);

    /**
     * Create user.
     * @param credentials Account data with login, password and role (if admin).
     * @return new user id.
     */
    @POST("users/")
    Completable createUser(@Body CredentialsModel credentials);
}
