package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 4/9/2018.
 */

public class JsonMetadata implements Parcelable {
    @Expose
    @SerializedName("tags")
    public List<String> tags;
    @Expose
    @SerializedName("content")
    public Content content;
    @Expose
    @SerializedName("app")
    public String app;

    protected JsonMetadata(Parcel in) {
        if (in.readByte() == 0x01) {
            tags = new ArrayList<String>();
            in.readList(tags, String.class.getClassLoader());
        } else {
            tags = null;
        }
        content = (Content) in.readValue(Content.class.getClassLoader());
        app = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tags == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tags);
        }
        dest.writeValue(content);
        dest.writeString(app);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<JsonMetadata> CREATOR = new Parcelable.Creator<JsonMetadata>() {
        @Override
        public JsonMetadata createFromParcel(Parcel in) {
            return new JsonMetadata(in);
        }

        @Override
        public JsonMetadata[] newArray(int size) {
            return new JsonMetadata[size];
        }
    };

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
