package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/21/2017.
 */

public class CreateUserRequest {


    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("token")
    public String token;
    @SerializedName("org_id")
    public int org_id;

    public CreateUserRequest(String email, String username, String full_name, String token, int org_id) {
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.token = token;
        this.org_id = org_id;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", token='" + token + '\'' +
                ", org_id=" + org_id +
                '}';
    }
}
