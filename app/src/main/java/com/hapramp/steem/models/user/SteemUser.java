package com.hapramp.steem.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SteemUser {

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("status")
    @Expose
    public String status;

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "SteemUser{" +
          "user=" + user +
          ", status='" + status + '\'' +
          '}';
    }
}
