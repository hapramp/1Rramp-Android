package com.hapramp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunitySelectionServerUpdateBody {

    @SerializedName("communities")
    private List<Integer> mCommunities;

    public CommunitySelectionServerUpdateBody(List<Integer> mCommunities) {
        this.mCommunities = mCommunities;
    }

    public List<Integer> getmCommunities() {
        return mCommunities;
    }

    public void setmCommunities(List<Integer> mCommunities) {
        this.mCommunities = mCommunities;
    }
}
