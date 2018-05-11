package com.hapramp.viewmodel.login;

import android.arch.lifecycle.ViewModel;

import com.hapramp.model.login.LoginManager;
import com.hapramp.ui.callbacks.login.LoginCallbacks;

/**
 * Created by Ankit on 5/6/2018.
 */

public class LoginViewModel extends ViewModel {

    LoginManager loginManager;

    public LoginViewModel() {
        loginManager = new LoginManager();
    }

    public void attemptLogin(String username , String ppk , LoginCallbacks loginCallbacks){
        loginManager.attemptLogin(username,ppk,loginCallbacks);
    }


}
