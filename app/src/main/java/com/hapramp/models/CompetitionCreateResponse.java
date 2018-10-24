package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionCreateResponse {

  @Expose
  @SerializedName("field")
  private String mField = "";
  @Expose
  @SerializedName("status")
  private String mStatus;
  @Expose
  @SerializedName("message")
  private String mMessage = "";
  @Expose
  @SerializedName("code")
  private int mCode;

  public CompetitionCreateResponse(String mField, String mStatus, String mMessage, int mCode) {
    this.mField = mField;
    this.mStatus = mStatus;
    this.mMessage = mMessage;
    this.mCode = mCode;
  }

  public String getmField() {
    return mField;
  }

  public String getmStatus() {
    return mStatus;
  }

  public String getmMessage() {
    return mMessage;
  }

  public int getmCode() {
    return mCode;
  }

  @Override
  public String toString() {
    return "CompetitionCreateResponse{" +
      "mField='" + mField + '\'' +
      ", mStatus='" + mStatus + '\'' +
      ", mMessage='" + mMessage + '\'' +
      ", mCode=" + mCode +
      '}';
  }
}
