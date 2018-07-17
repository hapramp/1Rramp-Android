package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowingsResponse {

  @SerializedName("result")
  private List<Result> mResult;
  @SerializedName("id")
  private int mId;

  public FollowingsResponse(List<Result> mResult, int mId) {
    this.mResult = mResult;
    this.mId = mId;
  }

  public List<Result> getmResult() {
    return mResult;
  }

  public void setmResult(List<Result> mResult) {
    this.mResult = mResult;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public static class Result {
    @SerializedName("what")
    private List<String> mWhat;
    @SerializedName("following")
    private String mFollowing;
    @SerializedName("follower")
    private String mFollower;

    public Result(List<String> mWhat, String mFollowing, String mFollower) {
      this.mWhat = mWhat;
      this.mFollowing = mFollowing;
      this.mFollower = mFollower;
    }

    public List<String> getmWhat() {
      return mWhat;
    }

    public void setmWhat(List<String> mWhat) {
      this.mWhat = mWhat;
    }

    public String getmFollowing() {
      return mFollowing;
    }

    public void setmFollowing(String mFollowing) {
      this.mFollowing = mFollowing;
    }

    public String getmFollower() {
      return mFollower;
    }

    public void setmFollower(String mFollower) {
      this.mFollower = mFollower;
    }
  }
}
