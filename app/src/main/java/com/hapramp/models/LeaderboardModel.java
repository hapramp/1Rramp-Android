package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaderboardModel {

  @Expose
  @SerializedName("winners")
  private List<Winners> mWinners;

  public List<Winners> getmWinners() {
    return mWinners;
  }

  public void setmWinners(List<Winners> mWinners) {
    this.mWinners = mWinners;
  }

  public static class Entries {
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

  public static class Winners {
    @Expose
    @SerializedName("author")
    private String mAuthor;
    @Expose
    @SerializedName("score")
    private float mScore;
    @Expose
    @SerializedName("entries")
    private List<Entries> mEntries;

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
}
