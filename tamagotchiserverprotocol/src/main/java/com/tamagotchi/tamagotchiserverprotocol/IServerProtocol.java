package com.tamagotchi.tamagotchiserverprotocol;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IServerProtocol {
    void CreateAccount(String login, String password);
    void GetAccountsTest() throws Exception;
}
