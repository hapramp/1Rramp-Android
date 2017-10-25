package com.hapramp.interfaces;

import com.hapramp.models.response.FetchUserResponse;

/**
 * Created by Ankit on 10/22/2017.
 */

public interface FetchUserCallback {
    void onUserFetched(FetchUserResponse userResponse);
    void onUserFetchedError();
    void onUserNotExists();

}
