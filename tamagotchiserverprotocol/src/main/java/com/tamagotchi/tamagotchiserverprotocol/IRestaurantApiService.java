package com.tamagotchi.tamagotchiserverprotocol;

import com.tamagotchi.tamagotchiserverprotocol.models.AccountModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRestaurantApiService {

    @GET("accounts")
    Call<List<AccountModel>> getAccounts();

    /*UserModel CreateUser(String login, String password, JsonHttpResponseHandler handler) throws Exception;
    List<UserModel> GetAllUsers(JsonHttpResponseHandler handler) throws Exception;

    // Аутентификация пользователя. Возвращает Jwt токен.
    String Authenticate(String login, String password, JsonHttpResponseHandler handler) throws Exception;
    void UpdateUser() throws Exception;*/
}
