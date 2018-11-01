package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionEntryConfirmationBody {

  @Expose
  @SerializedName("permlink")
  private String mPermlink;

  public CompetitionEntryConfirmationBody(String mPermlink) {
    this.mPermlink = mPermlink;
  }

  public String getmPermlink() {
    return mPermlink;
  }

  public void setmPermlink(String mPermlink) {
    this.mPermlink = mPermlink;
  }
}
