package com.tamagotchi.restaurantclientapplication.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.model.LoggedInUser;
import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private AccountsRepository accountsRepository;
    private AuthenticationService authenticationService;

    LoginViewModel(AccountsRepository accountsRepository, AuthenticationService authenticationService) {
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
        LiveData<Result> resultLiveData = authenticationService.authenticate(loginInfo);

        final Observer<Result> createdObserver = result -> {
            if (result instanceof Result.Success) {
                loginResult.setValue(new LoginResult(new LoggedInUserView("You")));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        };

        resultLiveData.observeForever(createdObserver);
    }

    public void create(String username, String password) {

        LiveData<Result> resultLiveData = accountsRepository.createAccount(new SignInfoModel(username, password));

        final Observer<Result> createdObserver = result -> {
            if (result instanceof Result.Success) {
                loginResult.setValue(new LoginResult(new LoggedInUserView("You")));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        };

        resultLiveData.observeForever(createdObserver);
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
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }
}
