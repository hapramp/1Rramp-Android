package com.hapramp.interfaces;

import com.hapramp.models.UserResponse;

/**
 * Created by Ankit on 12/13/2017.
 */

public interface UserFetchCallback {

    void onUserFetched(int commentPosition , UserResponse response);
    void onUserFetchError();

}
