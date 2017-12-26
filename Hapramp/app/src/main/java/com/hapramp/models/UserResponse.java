package com.hapramp.models;

import com.google.gson.annotations.SerializedName;
import com.hapramp.models.response.UserModel;

import java.util.List;

/**
 * Created by Ankit on 11/16/2017.
 */

public class UserResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("organization")
    public Organization organization;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("skills")
    public List<UserModel.Skills> skills;
    @SerializedName("karma")
    public int karma;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("bio")
    public String bio;
    @SerializedName("hapcoins")
    public int hapcoins;

    public static class Organization {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("username")
        public String username;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;
    }


    public UserResponse(int id, String email, String username, Organization organization, String full_name, List<UserModel.Skills> skills, int karma, String image_uri, String bio, int hapcoins) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.organization = organization;
        this.full_name = full_name;
        this.skills = skills;
        this.karma = karma;
        this.image_uri = image_uri;
        this.bio = bio;
        this.hapcoins = hapcoins;
    }
}
