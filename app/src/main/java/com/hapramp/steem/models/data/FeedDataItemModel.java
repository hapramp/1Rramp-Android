package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/9/2018.
 */


public class FeedDataItemModel implements Parcelable {

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
  @Expose
  @SerializedName("type")
  public String type;
  @Expose
  @SerializedName("content")
  public String content;
  @Expose
  @SerializedName("height")
  public String height;
  @Expose
  @SerializedName("width")
  public String width;
  @Expose
  @SerializedName("caption")
  public String caption;

  public FeedDataItemModel(String content, String type) {
    this.type = type;
    this.content = content;
  }

  protected FeedDataItemModel(Parcel in) {

    type = in.readString();
    content = in.readString();
    height = in.readString();
    width = in.readString();
    caption = in.readString();

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type);
    dest.writeString(content);
    dest.writeString(height);
    dest.writeString(width);
    dest.writeString(caption);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getContentType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "FeedDataItemModel{" +
      "type='" + type + '\'' +
      ", content='" + content + '\'' +
      '}';
  }
}
