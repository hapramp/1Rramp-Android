package com.hapramp.models;

import com.google.gson.annotations.SerializedName;

public class VerifiedToken {
  @SerializedName("token")
  public String token;

  public VerifiedToken(String token) {
    this.token = token;
  }
}
