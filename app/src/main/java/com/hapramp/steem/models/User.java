package com.hapramp.steem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 3/27/2018.
 */

public class User {
  @SerializedName("username")
  private String username;
  @SerializedName("full_name")
  private String fullname;
  @SerializedName("location")
  private String location;
  @SerializedName("profile_image")
  private String profile_image;
  @SerializedName("cover_image")
  private String cover_image;
  @SerializedName("about")
  private String about;
  @SerializedName("website")
  private String website;
  @SerializedName("post_count")
  private int postCount;
  @SerializedName("reputation")
  private long reputation;
  @SerializedName("created")
  private String created;
  @SerializedName("comment_count")
  private int commentCount;
  @SerializedName("can_vote")
  private boolean canVote;
  @SerializedName("voting_power")
  private int votingPower;
  @Expose
  @SerializedName("savings_sbd_balance")
  private String savings_sbd_balance;
  @Expose
  @SerializedName("sbd_balance")
  private String sbd_balance;
  @Expose
  @SerializedName("savings_balance")
  private String savings_balance;
  @Expose
  @SerializedName("balance")
  private String balance;
  @Expose
  @SerializedName("vesting_shares")
  private String vesting_share;
  @Expose
  @SerializedName("reward_sbd_balance")
  private String sbdRewardBalance;

  @Expose
  @SerializedName("reward_steem_balance")
  private String steemRewardBalance;

  @Expose
  @SerializedName("reward_vesting_balance")
  private String vestsRewardBalance;

  public User() {
  }

  public User(String username,
              String fullname,
              String location,
              String profile_image,
              String cover_image,
              String about,
              String website,
              int postCount,
              long reputation,
              String created,
              int commentCount,
              boolean canVote,
              int votingPower,
              String savings_sbd_balance,
              String sbd_balance,
              String savings_balance,
              String balance,
              String vesting_share,
              String sbdRewardBalance,
              String steemRewardBalance,
              String vestsRewardBalance) {
    this.username = username;
    this.fullname = fullname;
    this.location = location;
    this.profile_image = profile_image;
    this.cover_image = cover_image;
    this.about = about;
    this.website = website;
    this.postCount = postCount;
    this.reputation = reputation;
    this.created = created;
    this.commentCount = commentCount;
    this.canVote = canVote;
    this.votingPower = votingPower;
    this.savings_sbd_balance = savings_sbd_balance;
    this.sbd_balance = sbd_balance;
    this.savings_balance = savings_balance;
    this.balance = balance;
    this.vesting_share = vesting_share;
    this.sbdRewardBalance = sbdRewardBalance;
    this.steemRewardBalance = steemRewardBalance;
    this.vestsRewardBalance = vestsRewardBalance;
  }

  public String getSbdRewardBalance() {
    return sbdRewardBalance;
  }

  public void setSbdRewardBalance(String sbdRewardBalance) {
    this.sbdRewardBalance = sbdRewardBalance;
  }

  public String getSteemRewardBalance() {
    return steemRewardBalance;
  }

  public void setSteemRewardBalance(String steemRewardBalance) {
    this.steemRewardBalance = steemRewardBalance;
  }

  public String getVestsRewardBalance() {
    return vestsRewardBalance;
  }

  public void setVestsRewardBalance(String vestsRewardBalance) {
    this.vestsRewardBalance = vestsRewardBalance;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  public String getSavings_sbd_balance() {
    return savings_sbd_balance;
  }

  public void setSavings_sbd_balance(String savings_sbd_balance) {
    this.savings_sbd_balance = savings_sbd_balance;
  }

  public String getSbd_balance() {
    return sbd_balance;
  }

  public void setSbd_balance(String sbd_balance) {
    this.sbd_balance = sbd_balance;
  }

  public String getSavings_balance() {
    return savings_balance;
  }

  public void setSavings_balance(String savings_balance) {
    this.savings_balance = savings_balance;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getVesting_share() {
    return vesting_share;
  }

  public void setVesting_share(String vesting_share) {
    this.vesting_share = vesting_share;
  }

  public boolean isCanVote() {
    return canVote;
  }

  public void setCanVote(boolean canVote) {
    this.canVote = canVote;
  }

  public int getVotingPower() {
    return votingPower;
  }

  public void setVotingPower(int votingPower) {
    this.votingPower = votingPower;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getUsername() {
    return username;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getProfile_image() {
    return profile_image;
  }

  public void setProfile_image(String profile_image) {
    this.profile_image = profile_image;
  }

  public String getCover_image() {
    return cover_image;
  }

  public void setCover_image(String cover_image) {
    this.cover_image = cover_image;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public int getPostCount() {
    return postCount;
  }

  public void setPostCount(int postCount) {
    this.postCount = postCount;
  }

  public long getReputation() {
    return reputation;
  }

  public void setReputation(long reputation) {
    this.reputation = reputation;
  }

  @Override
  public String toString() {
    return "User{" +
      "username='" + username + '\'' +
      ", fullname='" + fullname + '\'' +
      ", location='" + location + '\'' +
      ", profile_image='" + profile_image + '\'' +
      ", cover_image='" + cover_image + '\'' +
      ", about='" + about + '\'' +
      ", postCount=" + postCount +
      ", reputation='" + reputation + '\'' +
      '}';
  }
}
