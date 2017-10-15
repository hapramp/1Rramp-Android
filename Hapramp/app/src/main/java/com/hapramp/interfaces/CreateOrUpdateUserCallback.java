package com.hapramp.interfaces;

/**
 * Created by Ankit on 10/11/2017.
 */

public interface CreateOrUpdateUserCallback {
    void onUserCreated();
    void onFailedToCreateOrUpdateUser();
}
