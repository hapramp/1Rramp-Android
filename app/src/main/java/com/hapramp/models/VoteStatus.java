package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/4/2018.
 */

public class VoteStatus {
  @Expose
  @SerializedName("status")
  public String status;

  public VoteStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
