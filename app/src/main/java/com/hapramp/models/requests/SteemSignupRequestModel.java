package com.hapramp.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 2/17/2018.
 */

public class SteemSignupRequestModel {

  @SerializedName("username")
  @Expose
  private String username;

  public SteemSignupRequestModel(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
