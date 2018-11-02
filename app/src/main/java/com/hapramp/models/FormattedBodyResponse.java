package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormattedBodyResponse {

  @Expose
  @SerializedName("body")
  private String mBody;
  @Expose
  @SerializedName("id")
  private String mId;

  public FormattedBodyResponse() {
  }

  public FormattedBodyResponse(String mBody, String mId) {
    this.mBody = mBody;
    this.mId = mId;
  }

  public String getmBody() {
    return mBody;
  }

  public void setmBody(String mBody) {
    this.mBody = mBody;
  }

  public String getmId() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }
}
