package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DraftUploadResponse {

  @Expose
  @SerializedName("last_modified_at")
  private String mLastModifiedAt;
  @Expose
  @SerializedName("created_at")
  private String mCreatedAt;
  @Expose
  @SerializedName("draft_type")
  private String mDraftType;
  @Expose
  @SerializedName("json_metadata")
  private String mJsonMetadata;
  @Expose
  @SerializedName("body")
  private String mBody;
  @Expose
  @SerializedName("title")
  private String mTitle;
  @Expose
  @SerializedName("id")
  private int mId;

  public String getmLastModifiedAt() {
    return mLastModifiedAt;
  }

  public void setmLastModifiedAt(String mLastModifiedAt) {
    this.mLastModifiedAt = mLastModifiedAt;
  }

  public String getmCreatedAt() {
    return mCreatedAt;
  }

  public void setmCreatedAt(String mCreatedAt) {
    this.mCreatedAt = mCreatedAt;
  }

  public String getmDraftType() {
    return mDraftType;
  }

  public void setmDraftType(String mDraftType) {
    this.mDraftType = mDraftType;
  }

  public String getmJsonMetadata() {
    return mJsonMetadata;
  }

  public void setmJsonMetadata(String mJsonMetadata) {
    this.mJsonMetadata = mJsonMetadata;
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

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }
}
