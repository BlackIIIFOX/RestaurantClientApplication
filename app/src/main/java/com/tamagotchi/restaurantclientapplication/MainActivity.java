package com.tamagotchi.restaurantclientapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tamagotchi.tamagotchiserverprotocol.ServerProtocol;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ServerProtocol protocol = new ServerProtocol();
        protocol.CreateAccount("asd", "asd");
    }
}
