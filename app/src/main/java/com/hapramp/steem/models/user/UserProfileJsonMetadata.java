package com.hapramp.steem.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 3/27/2018.
 */


public class UserProfileJsonMetadata {

    @SerializedName("profile")
    @Expose
    public Profile profile;

    public Profile getProfile() {
        return profile;
    }
}