package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

public class PreProcessingModel {

  @SerializedName("full_permlink")
  private String mFullPermlink;
  @SerializedName("content")
  private String mContent;

  public PreProcessingModel(String mFullPermlink, String mContent) {
    this.mFullPermlink = mFullPermlink;
    this.mContent = mContent;
  }

  public String getmFullPermlink() {
    return mFullPermlink;
  }

  public void setmFullPermlink(String mFullPermlink) {
    this.mFullPermlink = mFullPermlink;
  }

  public String getmContent() {
    return mContent;
  }

  public void setmContent(String mContent) {
    this.mContent = mContent;
  }
}
