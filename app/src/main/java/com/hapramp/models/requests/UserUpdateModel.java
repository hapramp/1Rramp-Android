package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/22/2017.
 */

public class UserUpdateModel {

    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("org_id")
    public int org_id;

    public UserUpdateModel(String email, String username, String full_name, int org_id) {
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.org_id = org_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    @Override
    public String toString() {
        return "UserUpdateModel{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", org_id=" + org_id +
                '}';
    }
}
