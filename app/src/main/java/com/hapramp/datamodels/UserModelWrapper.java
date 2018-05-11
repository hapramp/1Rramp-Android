package com.hapramp.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.datamodels.response.UserModel;

import java.util.List;

/**
 * Created by Ankit on 4/5/2018.
 */

public class UserModelWrapper {
    @Expose
    @SerializedName("users")
    public List<UserModel> users;

    public UserModelWrapper(List<UserModel> users) {
        this.users = users;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
