package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JudgeModel implements Parcelable {
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<JudgeModel> CREATOR = new Parcelable.Creator<JudgeModel>() {
    @Override
    public JudgeModel createFromParcel(Parcel in) {
      return new JudgeModel(in);
    }

    @Override
    public JudgeModel[] newArray(int size) {
      return new JudgeModel[size];
    }
  };
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

  protected JudgeModel(Parcel in) {
    mBio = in.readString();
    mUsername = in.readString();
    mFullName = in.readString();
    mId = in.readInt();
    isSelected = in.readByte() != 0x00;
  }

  public JudgeModel() {

  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mBio);
    dest.writeString(mUsername);
    dest.writeString(mFullName);
    dest.writeInt(mId);
    dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
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
}
