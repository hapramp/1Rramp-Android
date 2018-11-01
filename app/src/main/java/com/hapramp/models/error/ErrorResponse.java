package com.hapramp.models.error;

public class ErrorResponse {

  @com.google.gson.annotations.Expose
  @com.google.gson.annotations.SerializedName("status")
  private String mStatus;
  @com.google.gson.annotations.Expose
  @com.google.gson.annotations.SerializedName("message")
  private String mMessage;
  @com.google.gson.annotations.Expose
  @com.google.gson.annotations.SerializedName("code")
  private int mCode;

  public ErrorResponse() {
  }

  public ErrorResponse(String mStatus, String mMessage, int mCode) {
    this.mStatus = mStatus;
    this.mMessage = mMessage;
    this.mCode = mCode;
  }

  public String getmStatus() {
    return mStatus;
  }

  public void setmStatus(String mStatus) {
    this.mStatus = mStatus;
  }

  public String getmMessage() {
    return mMessage;
  }

  public void setmMessage(String mMessage) {
    this.mMessage = mMessage;
  }

  public int getmCode() {
    return mCode;
  }

  public void setmCode(int mCode) {
    this.mCode = mCode;
  }
}
