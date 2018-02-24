package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostStructureModel {

    @SerializedName("data")
    private List<Data> mData;
    @SerializedName("type")
    private String mType;

    public static class Data {
        @SerializedName("content")
        private String mContent;
        @SerializedName("type")
        private String mType;

        public Data(String mContent, String mType) {
            this.mContent = mContent;
            this.mType = mType;
        }

    }

    public PostStructureModel(List<Data> mData, String mType) {
        this.mData = mData;
        this.mType = mType;
    }
}
