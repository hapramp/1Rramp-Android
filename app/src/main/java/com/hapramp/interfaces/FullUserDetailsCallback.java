package com.hapramp.interfaces;

import com.hapramp.models.response.UserModel;

/**
 * Created by Ankit on 10/29/2017.
 */

public interface FullUserDetailsCallback {
    void onFullUserDetailsFetched(UserModel userModel);
    void onFullUserDetailsFetchError();
}
