package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LookupAccount {
  @Expose
  @SerializedName("result")
  private List<String> mResult;

  public List<String> getmResult() {
    return mResult;
  }

  public void setmResult(List<String> mResult) {
    this.mResult = mResult;
  }
}
