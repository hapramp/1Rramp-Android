package com.hapramp.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardModel implements Parcelable {

  @Expose
  @SerializedName("winners")
  private ArrayList<Winners> mWinners;

  public ArrayList<Winners> getmWinners() {
    return mWinners;
  }

  public void setmWinners(ArrayList<Winners> mWinners) {
    this.mWinners = mWinners;
  }


  public static class Entries implements Parcelable {
    @Expose
    @SerializedName("competition")
    private String mCompetition;
    @Expose
    @SerializedName("prize")
    private String mPrize;
    @Expose
    @SerializedName("rank")
    private int mRank;
    @Expose
    @SerializedName("permlink")
    private String mPermlink;

    protected Entries(Parcel in) {
      mCompetition = in.readString();
      mPrize = in.readString();
      mRank = in.readInt();
      mPermlink = in.readString();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(mCompetition);
      dest.writeString(mPrize);
      dest.writeInt(mRank);
      dest.writeString(mPermlink);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Entries> CREATOR = new Parcelable.Creator<Entries>() {
      @Override
      public Entries createFromParcel(Parcel in) {
        return new Entries(in);
      }

      @Override
      public Entries[] newArray(int size) {
        return new Entries[size];
      }
    };

    public String getmCompetition() {
      return mCompetition;
    }

    public void setmCompetition(String mCompetition) {
      this.mCompetition = mCompetition;
    }

    public String getmPrize() {
      return mPrize;
    }

    public void setmPrize(String mPrize) {
      this.mPrize = mPrize;
    }

    public int getmRank() {
      return mRank;
    }

    public void setmRank(int mRank) {
      this.mRank = mRank;
    }

    public String getmPermlink() {
      return mPermlink;
    }

    public void setmPermlink(String mPermlink) {
      this.mPermlink = mPermlink;
    }
  }

  public static class Winners implements Parcelable {
    @Expose
    @SerializedName("author")
    private String mAuthor;
    @Expose
    @SerializedName("score")
    private float mScore;
    @Expose
    @SerializedName("entries")
    private List<Entries> mEntries;

    protected Winners(Parcel in) {
      mAuthor = in.readString();
      mScore = in.readFloat();
      if (in.readByte() == 0x01) {
        mEntries = new ArrayList<Entries>();
        in.readList(mEntries, Entries.class.getClassLoader());
      } else {
        mEntries = null;
      }
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(mAuthor);
      dest.writeFloat(mScore);
      if (mEntries == null) {
        dest.writeByte((byte) (0x00));
      } else {
        dest.writeByte((byte) (0x01));
        dest.writeList(mEntries);
      }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Winners> CREATOR = new Parcelable.Creator<Winners>() {
      @Override
      public Winners createFromParcel(Parcel in) {
        return new Winners(in);
      }

      @Override
      public Winners[] newArray(int size) {
        return new Winners[size];
      }
    };
    /**
     * format user avatar image url
     * @return formatted username
     */
    public String avatarUrl(Context context) {
      return String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
        mAuthor);
    }

    public String getmAuthor() {
      return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
      this.mAuthor = mAuthor;
    }

    public float getmScore() {
      return mScore;
    }

    public void setmScore(float mScore) {
      this.mScore = mScore;
    }

    public List<Entries> getmEntries() {
      return mEntries;
    }

    public void setmEntries(List<Entries> mEntries) {
      this.mEntries = mEntries;
    }
  }

  @Override
  public String toString() {
    return "LeaderboardModel{" +
      "mWinners=" + mWinners +
      '}';
  }

  protected LeaderboardModel(Parcel in) {
    if (in.readByte() == 0x01) {
      mWinners = new ArrayList<Winners>();
      in.readList(mWinners, Winners.class.getClassLoader());
    } else {
      mWinners = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (mWinners == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(mWinners);
    }
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<LeaderboardModel> CREATOR = new Parcelable.Creator<LeaderboardModel>() {
    @Override
    public LeaderboardModel createFromParcel(Parcel in) {
      return new LeaderboardModel(in);
    }

    @Override
    public LeaderboardModel[] newArray(int size) {
      return new LeaderboardModel[size];
    }
  };
}
