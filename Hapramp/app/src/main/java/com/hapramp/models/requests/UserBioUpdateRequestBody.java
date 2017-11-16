package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/16/2017.
 */

public class UserBioUpdateRequestBody {

    @SerializedName("bio")
    public String bio;

    public UserBioUpdateRequestBody(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "UserBioUpdateRequestBody{" +
                "bio='" + bio + '\'' +
                '}';
    }
}
