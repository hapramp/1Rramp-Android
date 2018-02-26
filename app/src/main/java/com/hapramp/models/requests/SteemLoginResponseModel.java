package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;
import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;

import java.util.List;

/**
 * Created by Ankit on 2/17/2018.
 */

public class SteemLoginResponseModel {


    @SerializedName("communities")
    private List<CommunityModel> mCommunities;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("id")
    private int mId;
    @SerializedName("deviceID")
    private String deviceId;

    public SteemLoginResponseModel(List<CommunityModel> mCommunities, String mUsername, String mEmail, int mId , String deviceId) {
        this.mCommunities = mCommunities;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mId = mId;
        this.deviceId = deviceId;
    }

    public List<CommunityModel> getmCommunities() {
        return mCommunities;
    }

    public void setmCommunities(List<CommunityModel> mCommunities) {
        this.mCommunities = mCommunities;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
