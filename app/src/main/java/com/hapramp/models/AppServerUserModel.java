package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppServerUserModel {
  @Expose
  @SerializedName("communities")
  List<CommunityModel> communityList;
  @Expose
  @SerializedName("username")
  private String mUsername;
  @Expose
  @SerializedName("email")
  private String mEmail;
  @Expose
  @SerializedName("id")
  private int mId;

  public AppServerUserModel() {
  }

  public AppServerUserModel(List<CommunityModel> communityList, String mUsername, String mEmail, int mId) {
    this.communityList = communityList;
    this.mUsername = mUsername;
    this.mEmail = mEmail;
    this.mId = mId;
  }

  public String getmUsername() {
    return mUsername;
  }

  public void setmUsername(String mUsername) {
    this.mUsername = mUsername;
  }

  public String getmEmail() {
    return mEmail;
  }

  public void setmEmail(String mEmail) {
    this.mEmail = mEmail;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public List<CommunityModel> getCommunityList() {
    return communityList;
  }

  public void setCommunityList(List<CommunityModel> communityList) {
    this.communityList = communityList;
  }
}
