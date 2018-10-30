package com.hapramp.models;

public class ResourceCreditModel {
  private int votingPercentage;
  private int resourceCreditPercentage;
  private int voteALlowed;
  private int commentAllowed;
  private int transferAllowed;
  private double voteValue;

  public ResourceCreditModel() {
  }

  public double getVoteValue() {
    return voteValue;
  }

  public void setVoteValue(double voteValue) {
    this.voteValue = voteValue;
  }

  public int getResourceCreditPercentage() {
    return resourceCreditPercentage;
  }

  public void setResourceCreditPercentage(int resourceCreditPercentage) {
    this.resourceCreditPercentage = resourceCreditPercentage;
  }

  public int getVotingPercentage() {
    return votingPercentage;
  }

  public void setVotingPercentage(int votingPercentage) {
    this.votingPercentage = votingPercentage;
  }

  public int getVoteALlowed() {
    return voteALlowed;
  }

  public void setVoteALlowed(int voteALlowed) {
    this.voteALlowed = voteALlowed;
  }

  public int getCommentAllowed() {
    return commentAllowed;
  }

  public void setCommentAllowed(int commentAllowed) {
    this.commentAllowed = commentAllowed;
  }

  public int getTransferAllowed() {
    return transferAllowed;
  }

  public void setTransferAllowed(int transferAllowed) {
    this.transferAllowed = transferAllowed;
  }

  @Override
  public String toString() {
    return "ResourceCreditModel{" +
      "votingPercentage=" + votingPercentage +
      ", voteALlowed=" + voteALlowed +
      ", commentAllowed=" + commentAllowed +
      ", transferAllowed=" + transferAllowed +
      '}';
  }
}
