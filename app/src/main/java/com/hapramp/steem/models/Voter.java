package com.hapramp.steem.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Voter implements Parcelable {
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Voter> CREATOR = new Parcelable.Creator<Voter>() {
    @Override
    public Voter createFromParcel(Parcel in) {
      return new Voter(in);
    }

    @Override
    public Voter[] newArray(int size) {
      return new Voter[size];
    }
  };
  private String voter;
  private int percent;
  private long rshare;
  private String voteTime;

  protected Voter(Parcel in) {
    voter = in.readString();
    percent = in.readInt();
    rshare = in.readLong();
    voteTime = in.readString();
  }

  public Voter() {
  }

  public Voter(String voter, int percent, long rshare, String voteTime) {
    this.voter = voter;
    this.percent = percent;
    this.rshare = rshare;
    this.voteTime = voteTime;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(voter);
    dest.writeInt(percent);
    dest.writeLong(rshare);
    dest.writeString(voteTime);
  }

  public String getVoter() {
    return voter;
  }

  public void setVoter(String voter) {
    this.voter = voter;
  }

  public int getPercent() {
    return percent;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public long getRshare() {
    return rshare;
  }

  public void setRshare(long rshare) {
    this.rshare = rshare;
  }

  public void setVoteTime(String voteTime) {
    this.voteTime = voteTime;
  }

  @Override
  public String toString() {
    return "Voter{" +
      "voter='" + voter + '\'' +
      ", percent=" + percent +
      ", reputation='" + rshare + '\'' +
      ", voteTime='" + voteTime + '\'' +
      '}';
  }
}
