package com.tamagotchi.restaurantclientapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.tamagotchi.tamagotchiserverprotocol.ServerProtocol;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ServerProtocol protocol = new ServerProtocol();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        protocol.CreateAccount("asd", "asd");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                try {
                    protocol.GetAccountsTest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
