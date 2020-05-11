package com.tamagotchi.restaurantclientapplication.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.repositories.UsersRepository;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AccountExistException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthLoginException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UsersRepository accountsRepository;
    private AuthenticationService authenticationService;

    LoginViewModel(UsersRepository accountsRepository, AuthenticationService authenticationService) {
        this.accountsRepository = accountsRepository;
        this.authenticationService = authenticationService;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        LoginInfo loginInfo = new LoginInfo(username, password);
        authenticationService.signIn(loginInfo).subscribe(
                () -> loginResult.setValue(new LoginResult()),
                error -> {
                    if (error instanceof AuthLoginException) {
                        loginResult.setValue(new LoginResult(R.string.invalid_username));
                    } else if (error instanceof AuthPasswordException) {
                        loginResult.setValue(new LoginResult(R.string.auth_invalid_password));
                    } else {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                }
        );
    }

    public void create(String username, String password) {

        LoginInfo createInfo = new LoginInfo(username, password);

        accountsRepository.createAccount(createInfo).subscribe(
                () -> loginResult.setValue(new LoginResult()),
                error -> {
                    if (error instanceof AccountExistException) {
                        loginResult.setValue(new LoginResult(R.string.account_already_exist));
                    } else {
                        loginResult.setValue(new LoginResult(R.string.create_account_error));
                    }
                }
        );
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        return Patterns.PHONE.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}
