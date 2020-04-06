package com.tamagotchi.tamagotchiserverprotocol;

import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import java.util.List;

public interface IRestaurantApiService {
    UserModel CreateUser(String login, String password) throws Exception;
    List<UserModel> GetAllUsers() throws Exception;

    // Аутентификация пользователя. Возвращает Jwt токен.
    String Authenticate(String login, String password) throws Exception;
    void UpdateUser() throws Exception;
}
