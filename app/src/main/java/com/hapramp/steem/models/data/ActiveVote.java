package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/14/2018.
 */


public class ActiveVote implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<ActiveVote> CREATOR = new Parcelable.Creator<ActiveVote>() {
    @Override
    public ActiveVote createFromParcel(Parcel in) {
      return new ActiveVote(in);
    }

    @Override
    public ActiveVote[] newArray(int size) {
      return new ActiveVote[size];
    }
  };
  @SerializedName("voter")
  @Expose
  public String voter;
  @SerializedName("weight")
  @Expose
  public Long weight;
  @SerializedName("rshares")
  @Expose
  public Long rshares;
  @SerializedName("percent")
  @Expose
  public Long percent;
  @SerializedName("reputation")
  @Expose
  public String reputation;
  @SerializedName("time")
  @Expose
  public String time;

  protected ActiveVote(Parcel in) {
    voter = in.readString();
    weight = in.readByte() == 0x00 ? null : in.readLong();
    rshares = in.readByte() == 0x00 ? null : in.readLong();
    percent = in.readByte() == 0x00 ? null : in.readLong();
    reputation = in.readString();
    time = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(voter);
    if (weight == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(weight);
    }
    if (rshares == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(rshares);
    }
    if (percent == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(percent);
    }
    dest.writeString(reputation);
    dest.writeString(time);
  }
}
