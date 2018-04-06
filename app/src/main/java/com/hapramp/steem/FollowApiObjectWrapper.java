package com.hapramp.steem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;

/**
 * Created by Ankit on 4/6/2018.
 */

public class FollowApiObjectWrapper {
    @Expose
    @SerializedName("followings")
    public List<FollowApiObject> followings;

    public FollowApiObjectWrapper(List<FollowApiObject> followings) {
        this.followings = followings;
    }

    public List<FollowApiObject> getFollowings() {
        return followings;
    }

    public void setFollowings(List<FollowApiObject> followings) {
        this.followings = followings;
    }

    @Override
    public String toString() {
        return "FollowApiObjectWrapper{" +
                "followings=" + followings +
                '}';
    }
}
