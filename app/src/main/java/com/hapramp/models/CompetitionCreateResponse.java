package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionCreateResponse {

  @Expose
  @SerializedName("id")
  private String mId;

  public CompetitionCreateResponse() {
  }

  public CompetitionCreateResponse(String mId) {
    this.mId = mId;
  }

  public String getCompetitionID() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }
}
