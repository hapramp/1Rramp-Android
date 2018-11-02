package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JudgeModel implements Parcelable {
  @Expose
  @SerializedName("bio")
  private String mBio;
  @Expose
  @SerializedName("username")
  private String mUsername;
  @Expose
  @SerializedName("full_name")
  private String mFullName;
  @Expose
  @SerializedName("id")
  private int mId;

  private boolean isSelected;

  public JudgeModel() {
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  public String getmBio() {
    return mBio;
  }

  public void setmBio(String mBio) {
    this.mBio = mBio;
  }

  public String getmUsername() {
    return mUsername;
  }

  public void setmUsername(String mUsername) {
    this.mUsername = mUsername;
  }

  public String getmFullName() {
    return mFullName;
  }

  public void setmFullName(String mFullName) {
    this.mFullName = mFullName;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  @Override
  public String toString() {
    return "JudgeModel{" +
      "mBio='" + mBio + '\'' +
      ", mUsername='" + mUsername + '\'' +
      ", mFullName='" + mFullName + '\'' +
      ", mId=" + mId +
      ", isSelected=" + isSelected +
      '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mBio);
    dest.writeString(this.mUsername);
    dest.writeString(this.mFullName);
    dest.writeInt(this.mId);
    dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
  }

  protected JudgeModel(Parcel in) {
    this.mBio = in.readString();
    this.mUsername = in.readString();
    this.mFullName = in.readString();
    this.mId = in.readInt();
    this.isSelected = in.readByte() != 0;
  }

  public static final Creator<JudgeModel> CREATOR = new Creator<JudgeModel>() {
    @Override
    public JudgeModel createFromParcel(Parcel source) {
      return new JudgeModel(source);
    }

    @Override
    public JudgeModel[] newArray(int size) {
      return new JudgeModel[size];
    }
  };
}
