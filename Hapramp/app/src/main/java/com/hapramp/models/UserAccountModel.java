package com.hapramp.models;

/**
 * Created by Ankit on 9/7/2017.
 */

public class UserAccountModel {
    public String authToken;
    public String userEmail;
    public String userProfileName;

    public UserAccountModel(String authToken, String userEmail, String userProfileName) {
        this.authToken = authToken;
        this.userEmail = userEmail;
        this.userProfileName = userProfileName;
    }

    @Override
    public String toString() {
        return "UserAccountModel{" +
                "authToken='" + authToken + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userProfileName='" + userProfileName + '\'' +
                '}';
    }

}
