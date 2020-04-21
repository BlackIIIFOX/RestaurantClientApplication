package com.tamagotchi.restaurantclientapplication.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private Boolean success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult() {
        this.success = true;
    }

    @Nullable
    Boolean getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
