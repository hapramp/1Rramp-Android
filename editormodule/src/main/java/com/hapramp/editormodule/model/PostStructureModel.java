package com.hapramp.editormodule.model;

import java.util.ArrayList;
import java.util.List;

public class PostStructureModel {

    public List<Data> dataSeries;
    public String postType;

    public List<Data> getDataSeries() {
        return dataSeries;
    }

    public String getPostType() {
        return postType;
    }

    public PostStructureModel(List<Data> mData, String mType) {
        this.dataSeries = mData;
        this.postType = mType;
    }

    public static class Data {
        private String mContent;
        private String mContentType;

        public Data(String mContent, String mType) {
            this.mContent = mContent;
            this.mContentType = mType;
        }

        public String getmContent() {
            return mContent;
        }

        public String getmContentType() {
            return mContentType;
        }
    }

}
