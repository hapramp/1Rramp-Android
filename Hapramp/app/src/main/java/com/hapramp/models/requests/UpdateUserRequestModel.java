package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/12/2017.
 */

public class UpdateUserRequestModel {

    @SerializedName("email")
    public String email;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("token")
    public String token;

    public UpdateUserRequestModel(String email, String full_name, String token) {
        this.email = email;
        this.full_name = full_name;
        this.token = token;
    }

    @Override
    public String toString() {
        return "UpateUserRequestModel{" +
                "email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
