package com.hapramp.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 2/17/2018.
 */

public class SteemLoginRequestModel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("ppk_hash")
    @Expose
    private String ppkHash;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPpkHash() {
        return ppkHash;
    }

    public void setPpkHash(String ppkHash) {
        this.ppkHash = ppkHash;
    }

}
