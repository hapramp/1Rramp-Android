package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;
import com.hapramp.datamodels.CommunityModel;

import java.util.List;

/**
 * Created by Ankit on 10/29/2017.
 */

public class UserModel {


    @SerializedName("id") public int id;
    @SerializedName("email") public String email;
    @SerializedName("username") public String username;
    @SerializedName("deviceID") public String deviceID;
    @SerializedName("communities") public List<CommunityModel> communityModels;

    public UserModel(int id, String email, String username, String deviceID, List<CommunityModel> communityModels) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.deviceID = deviceID;
        this.communityModels = communityModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public List<CommunityModel> getCommunityModels() {
        return communityModels;
    }

    public void setCommunityModels(List<CommunityModel> communityModels) {
        this.communityModels = communityModels;
    }
}


