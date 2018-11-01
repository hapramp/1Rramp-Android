package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CommunityModel implements Parcelable {
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

  protected CommunityModel(Parcel in) {
    mDescription = in.readString();
    mImageUri = in.readString();
    mTag = in.readString();
    mColor = in.readString();
    mName = in.readString();
    mId = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mDescription);
    dest.writeString(mImageUri);
    dest.writeString(mTag);
    dest.writeString(mColor);
    dest.writeString(mName);
    dest.writeInt(mId);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<CommunityModel> CREATOR = new Parcelable.Creator<CommunityModel>() {
    @Override
    public CommunityModel createFromParcel(Parcel in) {
      return new CommunityModel(in);
    }

    @Override
    public CommunityModel[] newArray(int size) {
      return new CommunityModel[size];
    }
  };

  public CommunityModel(String mDescription, String mImageUri, String mTag, String mColor, String mName, int mId) {
    this.mDescription = mDescription;
    this.mImageUri = mImageUri;
    this.mTag = mTag;
    this.mColor = mColor;
    this.mName = mName;
    this.mId = mId;
  }

  public CommunityModel( String mColor, String mName) {
    this.mColor = mColor;
    this.mName = mName;
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

  public int getCommunityId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  @Override
  public String toString() {
    return "CommunityModel{" +
      "mDescription='" + mDescription + '\'' +
      ", mImageUri='" + mImageUri + '\'' +
      ", mTag='" + mTag + '\'' +
      ", mColor='" + mColor + '\'' +
      ", mName='" + mName + '\'' +
      ", mId=" + mId +
      '}';
  }
}
