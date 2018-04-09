package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/9/2018.
 */


public class FeedDataItemModel implements Parcelable {
    @Expose
    @SerializedName("type")
    public String type;
    @Expose
    @SerializedName("content")
    public String content;

    public FeedDataItemModel(String content, String type) {
        this.type = type;
        this.content = content;
    }

    protected FeedDataItemModel(Parcel in) {
        type = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FeedDataItemModel> CREATOR = new Parcelable.Creator<FeedDataItemModel>() {
        @Override
        public FeedDataItemModel createFromParcel(Parcel in) {
            return new FeedDataItemModel(in);
        }

        @Override
        public FeedDataItemModel[] newArray(int size) {
            return new FeedDataItemModel[size];
        }
    };


    public String getContentType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
