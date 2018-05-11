package com.hapramp.steem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.datamodels.CommunityModel;

import java.util.List;

public class CommunityListWrapper {

    @Expose
    @SerializedName("communities")
    List<CommunityModel> communityModels;

    public CommunityListWrapper(List<CommunityModel> communityModels) {
        this.communityModels = communityModels;
    }

    public List<CommunityModel> getCommunityModels() {
        return communityModels;
    }

    public void setCommunityModels(List<CommunityModel> communityModels) {
        this.communityModels = communityModels;
    }
}
