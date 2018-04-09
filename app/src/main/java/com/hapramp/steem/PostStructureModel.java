package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostStructureModel {

    @SerializedName("data")
    private List<Data> mDataSeries;
    @SerializedName("type")
    private String mType;

    public PostStructureModel(List<Data> mData, String mType) {
        this.mDataSeries = mData;
        this.mType = mType;
    }

    public List<Data> getDataSeries() {
        return mDataSeries;
    }

    public String getContentType() {
        return mType;
    }

    public static class Data {
        @SerializedName("content")
        private String mContent;
        @SerializedName("type")
        private String mType;

        public Data(String mContent, String mType) {
            this.mContent = mContent;
            this.mType = mType;
        }

        public String getContent() {
            return mContent;
        }

        public String getContentType() {
            return mType;
        }
    }

}
