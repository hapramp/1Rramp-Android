package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MCListWrapper {
  @Expose
  @SerializedName("mcList")
  ArrayList<MicroCommunity> microCommunities;

  public ArrayList<MicroCommunity> getMicroCommunities() {
    return microCommunities;
  }

  public void setMicroCommunities(ArrayList<MicroCommunity> microCommunities) {
    this.microCommunities = microCommunities;
  }
}
