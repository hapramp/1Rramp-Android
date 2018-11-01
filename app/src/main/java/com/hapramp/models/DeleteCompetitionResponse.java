package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteCompetitionResponse {

  @Expose
  @SerializedName("id")
  private String mId;
  @Expose
  @SerializedName("success")
  private boolean mSuccess;

  public DeleteCompetitionResponse(String mId, boolean mSuccess) {
    this.mId = mId;
    this.mSuccess = mSuccess;
  }

  public DeleteCompetitionResponse() {
  }

  public String getmId() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }

  public boolean ismSuccess() {
    return mSuccess;
  }

  public void setmSuccess(boolean mSuccess) {
    this.mSuccess = mSuccess;
  }
}
