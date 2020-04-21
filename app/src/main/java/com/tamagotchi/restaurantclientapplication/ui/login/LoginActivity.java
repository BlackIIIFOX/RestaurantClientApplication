package com.tamagotchi.restaurantclientapplication.ui.login;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.ui.BaseActivity;
import com.tamagotchi.restaurantclientapplication.ui.main.MainActivity;
import com.tamagotchi.restaurantclientapplication.ui.start.StartActivity;
import com.tamagotchi.restaurantclientapplication.R;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private boolean isNewAccount;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent activity2Intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(activity2Intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isNewAccount = intent.getBooleanExtra("isNewAccount", false);

        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        // Создаем
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }

            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }

            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            loginButton.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.VISIBLE);

            if (isNewAccount) {
                loginViewModel.create(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            else {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }

            loadingProgressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);

            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
                return;
            }

            if (isNewAccount) {
                Intent activity2Intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(activity2Intent);
                showMessage(R.string.create_success);
                finish();
            } else {
                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activity2Intent);
                finish();
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(@StringRes Integer message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
