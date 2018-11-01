package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CompetitionAdmin implements Parcelable {
  public String mUsername;
  public int mId;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mUsername);
    dest.writeInt(this.mId);
  }

  public CompetitionAdmin() {
  }

  protected CompetitionAdmin(Parcel in) {
    this.mUsername = in.readString();
    this.mId = in.readInt();
  }

  public static final Parcelable.Creator<CompetitionAdmin> CREATOR = new Parcelable.Creator<CompetitionAdmin>() {
    @Override
    public CompetitionAdmin createFromParcel(Parcel source) {
      return new CompetitionAdmin(source);
    }

    @Override
    public CompetitionAdmin[] newArray(int size) {
      return new CompetitionAdmin[size];
    }
  };

  @Override
  public String toString() {
    return "CompetitionAdmin{" +
      "mUsername='" + mUsername + '\'' +
      ", mId=" + mId +
      '}';
  }

  public String getmUsername() {
    return mUsername;
  }

  public void setmUsername(String mUsername) {
    this.mUsername = mUsername;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public static Creator<CompetitionAdmin> getCREATOR() {
    return CREATOR;
  }
}
