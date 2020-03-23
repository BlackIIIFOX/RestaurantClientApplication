package com.tamagotchi.tamagotchiserverprotocol;

import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import java.util.List;

public interface IServerProtocol {
    void CreateUser(UserModel userModel);
    List<UserModel> GetUsers() throws Exception;
    void UpdateUser() throws Exception;
}
