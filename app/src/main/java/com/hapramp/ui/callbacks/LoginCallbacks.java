package com.hapramp.ui.callbacks;

/**
 * Created by Ankit on 5/6/2018.
 */

public interface LoginCallbacks {
    void onInvalidCredentials();
    void onInvalidUsernameField(String msg);
    void onInvalidPostingKey();
    void onProcesing(String msg);
    void onLoginSuccess(boolean freshUser);
    void onLoginFailed(String msg);
    void onSignupFailed();
}
