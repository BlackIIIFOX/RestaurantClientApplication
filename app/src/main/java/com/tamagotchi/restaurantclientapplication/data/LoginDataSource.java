package com.tamagotchi.restaurantclientapplication.data;

import com.tamagotchi.restaurantclientapplication.data.model.LoggedInUser;
import com.tamagotchi.tamagotchiserverprotocol.IRestaurantApiService;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private IRestaurantApiService restaurantApiService;

    public LoginDataSource(IRestaurantApiService restaurantApiService) {

        this.restaurantApiService = restaurantApiService;
    }

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> login(String jwt) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public Result<LoggedInUser> create(String login, String password) {

        try {
            UserModel newUser = restaurantApiService.CreateUser(login, password);

            return new Result.Success<>(newUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
