package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/23/2017.
 */

public class UserStatsModel {

    @SerializedName("posts")
    public int posts;
    @SerializedName("rated")
    public int rated;
    @SerializedName("hapcoins")
    public float hapcoins;

    public UserStatsModel(int posts, int rated,float hapcoins) {
        this.posts = posts;
        this.rated = rated;
        this.hapcoins = hapcoins;
    }

    @Override
    public String toString() {
        return "UserStatsModel{" +
                "posts=" + posts +
                ", rated=" + rated +
                ", hapcoins=" + hapcoins +
                '}';
    }
}
