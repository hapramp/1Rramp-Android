package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

public class PostConfirmationModel {

  @SerializedName("full_permlink")
  String fullPermlink;

  public PostConfirmationModel(String fullPermlink) {
    this.fullPermlink = fullPermlink;
  }

  public String getFullPermlink() {
    return fullPermlink;
  }

  public void setFullPermlink(String fullPermlink) {
    this.fullPermlink = fullPermlink;
  }
}
