package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/4/2018.
 */

public class VoteModel {

  @Expose
  @SerializedName("username")
  public String usernam;

  @Expose
  @SerializedName("vote")
  public int vote;

  public VoteModel(String usernam, int vote) {
    this.usernam = usernam;
    this.vote = vote;
  }

  public String getUsernam() {
    return usernam;
  }

  public int getVote() {
    return vote;
  }
}
