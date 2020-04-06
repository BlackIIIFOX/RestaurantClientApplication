package com.tamagotchi.tamagotchiserverprotocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tamagotchi.tamagotchiserverprotocol.models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantApiService implements IRestaurantApiService {

    @Override
    public UserModel CreateUser(String login, String password) throws Exception {

        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("login is empty or null");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("password is empty or null");

        UserModel newUser = new UserModel();
        newUser.setLogin(login);
        newUser.setPassword(password);

        String accountData = new Gson().toJson(newUser);

        InputStreamReader response = SendRequest("POST", "/accounts/create", accountData);

        return new Gson().fromJson(response, UserModel.class);
    }

    @Override
    public List<UserModel> GetAllUsers() throws Exception {
        InputStreamReader response = SendRequest("GET", "/accounts/", null);
        return Arrays.asList(new Gson().fromJson(response, UserModel[].class));
    }

    @Override
    public String Authenticate(String login, String password) throws Exception {
        if (login == null || login.isEmpty())
            throw new IllegalArgumentException("login is empty or null");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("password is empty or null");

        UserModel newUser = new UserModel();
        newUser.setLogin(login);
        newUser.setPassword(password);

        String accountData = new Gson().toJson(newUser);

        InputStreamReader response = SendRequest("GET", "/accounts/authenticate", accountData);

        class AuthAnswer {
            @SerializedName("message")
            @Expose
            String Message;

            @SerializedName("token")
            @Expose
            String Token;
        }

        AuthAnswer authenticate =  new Gson().fromJson(response, AuthAnswer.class);

        return authenticate.Token;
    }

    private InputStreamReader SendRequest(String requestMethod, String apiUrl, String jsonData) throws Exception {
        URL restaurantEndpoint = new URL("http://192.168.56.1:3000/api" + apiUrl);

        // Create connection
        HttpURLConnection myConnection =
                (HttpURLConnection) restaurantEndpoint.openConnection();

        try {
            myConnection.setRequestMethod(requestMethod);

            if (jsonData != null) {
                // Enable writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(jsonData.getBytes());
            }

            int responseCode = myConnection.getResponseCode();

            if (responseCode == 200) {
                // Success
                // Further processing here
                InputStream responseBody = myConnection.getInputStream();

                return new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                //users = Arrays.asList(new Gson().fromJson(responseBodyReader, UserModel[].class));

                //users.add(new UserModel());

            } else {
                // Error handling code goes here

                if (responseCode == 401) {
                    throw new UnauthorizedException();
                }

                throw new ServerFaultException(Integer.toString(responseCode));
            }
        }
        finally {
            myConnection.disconnect();
        }
    }

    @Override
    public void UpdateUser() throws Exception {

    }

    /*
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
     */
}
