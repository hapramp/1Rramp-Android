package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.LocalConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 4/9/2018.
 */

public class JsonMetadata implements Parcelable {
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
  @Expose
  @SerializedName("tags")
  public List<String> tags;
  @Expose
  @SerializedName("content")
  public Content content;
  @Expose
  @SerializedName("app")
  public String app;

  public JsonMetadata(List<String> tags, Content content) {
    this.tags = tags;
    this.app = LocalConfig.APP_TAG;
    this.content = content;
  }

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

  public String getJson() {
    return new Gson().toJson(this, JsonMetadata.class);
  }

  public String getStringifiedJson() {
    return new Gson().toJson(this, JsonMetadata.class).replaceAll("\"", "\\\\\"");
  }
}
