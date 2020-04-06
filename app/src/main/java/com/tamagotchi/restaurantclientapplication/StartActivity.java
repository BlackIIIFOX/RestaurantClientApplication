package com.tamagotchi.restaurantclientapplication;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tamagotchi.restaurantclientapplication.ui.BaseActivity;
import com.tamagotchi.restaurantclientapplication.ui.login.LoginActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModel;
import com.tamagotchi.restaurantclientapplication.ui.main.MainViewModelFactory;

public class StartActivity extends BaseActivity {

    static final int LOGIN_REQUEST = 1; // The request code.
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mainViewModel = ViewModelProviders.of(this, new MainViewModelFactory())
                .get(MainViewModel.class);

        mainViewModel.StartServices();

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreate = findViewById(R.id.buttonCreate);
        ProgressBar loading = findViewById(R.id.loading);

        //buttonLogin.setVisibility(View.GONE);
        //buttonCreate.setVisibility(View.GONE);
        //loading.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(activity2Intent, LOGIN_REQUEST);
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), LoginActivity.class);
                activity2Intent.putExtra("isNewAccount", true);
                startActivityForResult(activity2Intent, LOGIN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
    }
}
