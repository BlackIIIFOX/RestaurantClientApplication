package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.ui.BaseActivity;
import com.tamagotchi.restaurantclientapplication.ui.login.LoginActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainActivity;

public class StartActivity extends BaseActivity {

    static final int LOGIN_REQUEST = 1; // The request code.
    private StartViewModel viewModel;
    private ProgressBar loading;
    private Button buttonCreate;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //mainViewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
        //mainViewModel.StartServices();

        viewModel = new ViewModelProvider(this, new StartViewModelFactory()).get(StartViewModel.class);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreate = findViewById(R.id.buttonCreate);
        loading = findViewById(R.id.loading);

        showProgressBarAndHideAuthButtons();

        buttonLogin.setOnClickListener(v -> {
            Intent activity2Intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(activity2Intent, LOGIN_REQUEST);
        });

        buttonCreate.setOnClickListener(v -> {
            Intent activity2Intent = new Intent(getApplicationContext(), LoginActivity.class);
            activity2Intent.putExtra("isNewAccount", true);
            startActivityForResult(activity2Intent, LOGIN_REQUEST);
        });

        viewModel.isAuthenticated().observe(this, isAuthState -> {
            if (isAuthState == null) {
                return;
            }

            if (isAuthState instanceof Result.Success) {
                // Открываем главное окно
                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activity2Intent);
            } else {
                showAuthButtonsAndHideProgressBar();
            }
        });

        viewModel.refreshAuthenticated();
    }

    private void updateLoginView() {

    }

    private void showAuthButtonsAndHideProgressBar() {
        buttonLogin.setVisibility(View.VISIBLE);
        buttonCreate.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void showProgressBarAndHideAuthButtons() {
        buttonLogin.setVisibility(View.GONE);
        buttonCreate.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            // Make sure the request was successful
            showProgressBarAndHideAuthButtons();
            viewModel.refreshAuthenticated();
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
