package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/16/2017.
 */

public class FollowRequestBody {

    @SerializedName("follow")
    public boolean follow;

    public FollowRequestBody(boolean follow) {
        this.follow = follow;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
