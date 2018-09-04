package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSearchResponse {
  @Expose
  @SerializedName("id")
  private int id;
  @Expose
  @SerializedName("result")
  private List<String> result;
  @Expose
  @SerializedName("jsonrpc")
  private String jsonrpc;

  public UserSearchResponse(int id, List<String> result, String jsonrpc) {
    this.id = id;
    this.result = result;
    this.jsonrpc = jsonrpc;
  }

  public List<String> getResult() {
    return result;
  }
}
