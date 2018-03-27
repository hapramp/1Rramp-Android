package com.hapramp.steem.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 3/27/2018.
 */


public class Profile {

    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("website")
    @Expose
    public String website;
    @SerializedName("about")
    @Expose
    public String about;

    public String getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getAbout() {
        return about;
    }
}