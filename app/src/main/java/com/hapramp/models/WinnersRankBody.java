package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinnersRankBody {
  @Expose
  @SerializedName("ranks")
  private List<Ranks> mRanks;

  public List<Ranks> getmRanks() {
    return mRanks;
  }

  public void setmRanks(List<Ranks> mRanks) {
    this.mRanks = mRanks;
  }

  public static class Ranks {
    @Expose
    @SerializedName("permlink")
    private String mPermlink;
    @Expose
    @SerializedName("rank")
    private int mRank;

    public Ranks(String mPermlink, int mRank) {
      this.mPermlink = mPermlink;
      this.mRank = mRank;
    }

    public String getmPermlink() {
      return mPermlink;
    }

    public void setmPermlink(String mPermlink) {
      this.mPermlink = mPermlink;
    }

    public int getmRank() {
      return mRank;
    }

    public void setmRank(int mRank) {
      this.mRank = mRank;
    }
  }
}
