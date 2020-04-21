package com.tamagotchi.restaurantclientapplication.ui.start;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.services.BootstrapService;
import com.tamagotchi.restaurantclientapplication.ui.BaseActivity;
import com.tamagotchi.restaurantclientapplication.ui.login.LoginActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainActivity;

public class StartActivity extends BaseActivity {

    private StartViewModel viewModel;
    private ProgressBar loading;
    private Button buttonCreate;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapService.getInstance().InitializeApplication();

        setContentView(R.layout.activity_start);

        viewModel = new ViewModelProvider(this, new StartViewModelFactory()).get(StartViewModel.class);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreate = findViewById(R.id.buttonCreate);
        loading = findViewById(R.id.loading);

        showProgressBarAndHideAuthButtons();

        buttonLogin.setOnClickListener(v -> {
            showLoginActivity(false);
        });

        buttonCreate.setOnClickListener(v -> {
            showLoginActivity(true);
        });

        viewModel.isAuthenticated().observe(this, isAuthState -> {
            if (isAuthState == null) {
                return;
            }

            if (isAuthState instanceof Result.Success) {
                // Открываем главное окно
                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activity2Intent);
                finish();
            } else {
                showAuthButtonsAndHideProgressBar();
            }
        });

        viewModel.refreshAuthenticated();
    }

    public void showLoginActivity(boolean isNewAccount) {
        Intent activity2Intent = new Intent(getApplicationContext(), LoginActivity.class);
        if (isNewAccount) {
            activity2Intent.putExtra("isNewAccount", true);
        }
        startActivity(activity2Intent);
        viewModel.isAuthenticated().removeObservers(this);
        finish();
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
    public void onBackPressed() {
        //finish();
    }
}
