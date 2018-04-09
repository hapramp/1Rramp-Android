package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.models.data.FeedDataItemModel;

import java.util.List;

public class PostStructureModel {

    @SerializedName("data")
    private List<FeedDataItemModel> mDataSeries;
    @SerializedName("type")
    private String mType;

    public PostStructureModel(List<FeedDataItemModel> mData, String mType) {
        this.mDataSeries = mData;
        this.mType = mType;
    }

    public List<FeedDataItemModel> getDataSeries() {
        return mDataSeries;
    }

    public String getContentType() {
        return mType;
    }

}
