package com.tamagotchi.restaurantclientapplication.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tamagotchi.restaurantclientapplication.R;
import com.tamagotchi.restaurantclientapplication.data.AccountsRepository;
import com.tamagotchi.restaurantclientapplication.data.Result;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AccountExistException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthLoginException;
import com.tamagotchi.restaurantclientapplication.data.exceptions.AuthPasswordException;
import com.tamagotchi.restaurantclientapplication.data.model.LoginInfo;
import com.tamagotchi.restaurantclientapplication.services.AuthenticationService;
import com.tamagotchi.tamagotchiserverprotocol.models.SignInfoModel;

import javax.security.auth.login.LoginException;

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
            if (result == null) {
                return;
            }

            if (result instanceof Result.Success) {
                loginResult.setValue(new LoginResult());
            } else {
                if (result instanceof Result.Error) {
                    Exception error = ((Result.Error) result).getError();
                    try {
                        throw error;
                    } catch (AuthLoginException e) {
                        loginResult.setValue(new LoginResult(R.string.invalid_username));
                    } catch (AuthPasswordException e) {
                        loginResult.setValue(new LoginResult(R.string.auth_invalid_password));
                    }
                    catch (Exception e) {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }
        };

        resultLiveData.observeForever(createdObserver);
    }

    public void create(String username, String password) {

        LoginInfo createInfo = new LoginInfo(username, password);

        LiveData<Result> resultLiveData = accountsRepository.createAccount(createInfo);

        final Observer<Result> createdObserver = result -> {
            if (result == null) {
                return;
            }

            if (result instanceof Result.Success) {
                loginResult.setValue(new LoginResult());
            } else {
                if (result instanceof Result.Error) {
                    Exception error = ((Result.Error) result).getError();
                    try {
                        throw error;
                    } catch (AccountExistException e) {
                        loginResult.setValue(new LoginResult(R.string.account_already_exist));
                    }
                    catch (Exception e) {
                        loginResult.setValue(new LoginResult(R.string.create_account_error));
                    }
                } else {
                    loginResult.setValue(new LoginResult(R.string.create_account_error));
                }
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
