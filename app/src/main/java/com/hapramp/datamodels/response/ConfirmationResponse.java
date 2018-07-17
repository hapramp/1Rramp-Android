package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/12/2018.
 */

public class ConfirmationResponse {
  @SerializedName("status")
  private String status;

  public ConfirmationResponse(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
