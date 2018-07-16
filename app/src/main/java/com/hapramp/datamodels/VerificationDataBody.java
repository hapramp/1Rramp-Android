package com.hapramp.datamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerificationDataBody {
  @Expose
  @SerializedName("email")
  public String email;
  @Expose
  @SerializedName("access_token")
  public String access_token;
  @Expose
  @SerializedName("username")
  public String username;

  public VerificationDataBody(String access_token, String username) {
    this.access_token = access_token;
    this.username = username;
  }
}
