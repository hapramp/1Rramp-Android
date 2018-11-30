package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DraftPostModel {

  @Expose
  @SerializedName("draft_type")
  private String draftType;
  @Expose
  @SerializedName("json_metadata")
  private String mJsonMetadata;
  @Expose
  @SerializedName("body")
  private String mBody;
  @Expose
  @SerializedName("title")
  private String mTitle;

  public String getmJsonMetadata() {
    return mJsonMetadata;
  }

  public void setmJsonMetadata(String mJsonMetadata) {
    this.mJsonMetadata = mJsonMetadata;
  }

  public String getDraftType() {
    return draftType;
  }

  public void setDraftType(String draftType) {
    this.draftType = draftType;
  }

  public String getmBody() {
    return mBody;
  }

  public void setmBody(String mBody) {
    this.mBody = mBody;
  }

  public String getmTitle() {
    return mTitle;
  }

  public void setmTitle(String mTitle) {
    this.mTitle = mTitle;
  }
}
