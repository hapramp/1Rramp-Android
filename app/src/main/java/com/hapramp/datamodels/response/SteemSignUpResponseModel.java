package com.hapramp.datamodels.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 2/17/2018.
 */

public class SteemSignUpResponseModel {

  @SerializedName("token")
  @Expose
  private String token;

  public SteemSignUpResponseModel(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
