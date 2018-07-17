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


public class Content implements Parcelable {
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
    @Override
    public Content createFromParcel(Parcel in) {
      return new Content(in);
    }

    @Override
    public Content[] newArray(int size) {
      return new Content[size];
    }
  };
  @Expose
  @SerializedName("type")
  public String type;
  @Expose
  @SerializedName("data")
  public List<FeedDataItemModel> data;

  public Content(List<FeedDataItemModel> data, String type) {
    this.data = data;
    this.type = type;
  }

  protected Content(Parcel in) {
    type = in.readString();
    if (in.readByte() == 0x01) {
      data = new ArrayList<FeedDataItemModel>();
      in.readList(data, FeedDataItemModel.class.getClassLoader());
    } else {
      data = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type);
    if (data == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(data);
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<FeedDataItemModel> getData() {
    return data;
  }

  public void setData(List<FeedDataItemModel> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Content{" +
      "type='" + type + '\'' +
      ", data=" + data +
      '}';
  }
}
