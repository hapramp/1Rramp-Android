package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowCountInfo {
  @Expose
  @SerializedName("id")
  private int id;
  @Expose
  @SerializedName("result")
  private Result result;
  @Expose
  @SerializedName("jsonrpc")
  private String jsonrpc;

  public Result getResult() {
    return result;
  }

  public static class Result {
    @Expose
    @SerializedName("following_count")
    private int following_count;
    @Expose
    @SerializedName("follower_count")
    private int follower_count;
    @Expose
    @SerializedName("account")
    private String account;

    public Result(int following_count, int follower_count, String account) {
      this.following_count = following_count;
      this.follower_count = follower_count;
      this.account = account;
    }

    public int getFollowing_count() {
      return following_count;
    }

    public int getFollower_count() {
      return follower_count;
    }

    public String getAccount() {
      return account;
    }
  }
}
