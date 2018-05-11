package com.hapramp.steem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.datamodels.CommunityModel;

import java.util.List;

/**
 * Created by Ankit on 3/15/2018.
 */

public class CommunitySelectionResponse {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("deviceID")
    @Expose
    private String deviceID;

    @SerializedName("communities")
    @Expose
    private List<CommunityModel> communities;

    public CommunitySelectionResponse(Integer id, String email, String username, String deviceID, List<CommunityModel> communities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.deviceID = deviceID;
        this.communities = communities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<CommunityModel> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunityModel> communities) {
        this.communities = communities;
    }
}
