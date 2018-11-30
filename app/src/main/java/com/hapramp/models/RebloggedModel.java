package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RebloggedModel {
  @SerializedName("result")
  @Expose
  private List<String> result = null;

  public List<String> getResult() {
    return result;
  }

  public void setResult(List<String> result) {
    this.result = result;
  }
}
