package com.tamagotchi.tamagotchiserverprotocol;

import android.content.Context;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.tamagotchi.tamagotchiserverprotocol.Models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ServerProtocol implements IServerProtocol {

    @Override
    public void CreateAccount(String login, String password) {

        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("login is empty or null");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("password is empty or null");

    }

    public void GetAccountsTest() throws Exception {
        URL restaurantEndpoint = new URL("http://192.168.56.1:3000/accounts");

        // Create connection
        HttpURLConnection myConnection =
                (HttpURLConnection) restaurantEndpoint.openConnection();

        myConnection.setRequestMethod("GET");

        /*
        * // Create the data
            String myData = "message=Hello";

            // Enable writing
            myConnection.setDoOutput(true);

            // Write the data
            myConnection.getOutputStream().write(myData.getBytes());
         */

        if (myConnection.getResponseCode() == 200) {
            // Success
            // Further processing here
            InputStream responseBody = myConnection.getInputStream();

            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");

            List<UserModel> users = Arrays.asList(new Gson().fromJson(responseBodyReader, UserModel[].class));

            users.add(new UserModel());

        } else {
            // Error handling code goes here
        }

        myConnection.disconnect();
    }

    private void SendRequest() throws IOException {
        URL url = new URL ("https://reqres.in/api/users");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}
