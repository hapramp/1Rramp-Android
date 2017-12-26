package com.hapramp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/26/2017.
 */

public class UserDataUpdateBody {


    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("bio")
    public String bio;
    @SerializedName("org_id")
    public int org_id;

    public UserDataUpdateBody(String email, String username, String full_name, String image_uri, String bio, int org_id) {
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.image_uri = image_uri;
        this.bio = bio;
        this.org_id = org_id;
    }

    @Override
    public String toString() {
        return "UserDataUpdateBody{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", image_uri='" + image_uri + '\'' +
                ", bio='" + bio + '\'' +
                ", org_id=" + org_id +
                '}';
    }
}
