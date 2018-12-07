package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppServerUserModel {
  @Expose
  @SerializedName("communities")
  List<CommunityModel> communityList;
  @Expose
  @SerializedName("micro_communities")
  List<MicroCommunity> microCommunities;
  @Expose
  @SerializedName("username")
  private String mUsername;
  @Expose
  @SerializedName("id")
  private int mId;
  @Expose
  @SerializedName("is_competition_user")
  private boolean isCompetitionUser;

  public List<CommunityModel> getCommunityList() {
    return communityList;
  }

  public void setCommunityList(List<CommunityModel> communityList) {
    this.communityList = communityList;
  }

  public String getmUsername() {
    return mUsername;
  }

  public void setmUsername(String mUsername) {
    this.mUsername = mUsername;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public boolean isCompetitionUser() {
    return isCompetitionUser;
  }

  public void setCompetitionUser(boolean competitionUser) {
    isCompetitionUser = competitionUser;
  }

  public List<MicroCommunity> getMicroCommunities() {
    return microCommunities;
  }

  public void setMicroCommunities(List<MicroCommunity> microCommunities) {
    this.microCommunities = microCommunities;
  }
}
