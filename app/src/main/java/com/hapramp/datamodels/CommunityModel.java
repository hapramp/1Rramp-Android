package com.hapramp.datamodels;

import com.google.gson.annotations.SerializedName;

public class CommunityModel {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("image_uri")
    private String mImageUri;
    @SerializedName("tag")
    private String mTag;
    @SerializedName("color")
    private String mColor;
    @SerializedName("name")
    private String mName;
    @SerializedName("id")
    private int mId;

    public CommunityModel(String mDescription, String mImageUri, String mTag, String mColor, String mName, int mId) {
        this.mDescription = mDescription;
        this.mImageUri = mImageUri;
        this.mTag = mTag;
        this.mColor = mColor;
        this.mName = mName;
        this.mId = mId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
