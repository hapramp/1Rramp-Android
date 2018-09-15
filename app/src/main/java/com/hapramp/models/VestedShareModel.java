package com.hapramp.models;

public class VestedShareModel {
  private String vestingShares;
  private String username;
  private int votingPower;
  private String receivedVestingShare;
  private String delegatedVestingShare;

  public VestedShareModel(String vestingShares,
                          String username,
                          int votingPower,
                          String receivedVestingShare,
                          String delegatedVestingShare) {
    this.vestingShares = vestingShares;
    this.username = username;
    this.votingPower = votingPower;
    this.receivedVestingShare = receivedVestingShare;
    this.delegatedVestingShare = delegatedVestingShare;
  }

  public String getVestingShares() {
    return vestingShares;
  }

  public void setVestingShares(String vestingShares) {
    this.vestingShares = vestingShares;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getVotingPower() {
    return votingPower;
  }

  public void setVotingPower(int votingPower) {
    this.votingPower = votingPower;
  }

  public String getReceivedVestingShare() {
    return receivedVestingShare;
  }

  public void setReceivedVestingShare(String receivedVestingShare) {
    this.receivedVestingShare = receivedVestingShare;
  }

  public String getDelegatedVestingShare() {
    return delegatedVestingShare;
  }

  public void setDelegatedVestingShare(String delegatedVestingShare) {
    this.delegatedVestingShare = delegatedVestingShare;
  }

  @Override
  public String toString() {
    return "VestedShareModel{" +
      "vestingShares='" + vestingShares + '\'' +
      ", username='" + username + '\'' +
      ", votingPower=" + votingPower +
      ", receivedVestingShare='" + receivedVestingShare + '\'' +
      ", delegatedVestingShare='" + delegatedVestingShare + '\'' +
      '}';
  }
}
