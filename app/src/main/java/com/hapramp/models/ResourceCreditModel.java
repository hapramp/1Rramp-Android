package com.hapramp.models;

public class ResourceCreditModel {
  private String maxMana;
  private String currentMana;
  private String commentPrice;
  private String votesPrice;
  private String transferPrice;
  private String resourceCreditPercentage;
  private String votingPowerPercentage;

  public ResourceCreditModel() {
  }

  public long getMaxMana() {
    return Long.valueOf(maxMana.replace(",", ""));
  }

  public void setMaxMana(String maxMana) {
    this.maxMana = maxMana;
  }

  public int getResourceCreditPercentage() {
    return Math.round(Float.valueOf(resourceCreditPercentage.replace("%","")));
  }

  public void setResourceCreditPercentage(String resourceCreditPercentage) {
    this.resourceCreditPercentage = resourceCreditPercentage;
  }

  public int getVotingPowerPercentage() {
    return Math.round(Float.valueOf(votingPowerPercentage.replace("%","")));
  }

  public void setVotingPowerPercentage(String votingPowerPercentage) {
    this.votingPowerPercentage = votingPowerPercentage;
  }

  public String getVotesAllowed() {
    long count = getCurrentMana() / getVotesPrice();
    String va = count > 100 ? "100+" : count + "";
    return va;
  }

  public long getCurrentMana() {
    return Long.valueOf(currentMana.replace(",", ""));
  }

  public void setCurrentMana(String currentMana) {
    this.currentMana = currentMana;
  }

  public long getVotesPrice() {
    return Long.valueOf(votesPrice.replace(",", ""));
  }

  public void setVotesPrice(String votesPrice) {
    this.votesPrice = votesPrice;
  }

  public String getCommentsAllowed() {
    long count = getCurrentMana() / getCommentPrice();
    String va = count > 100 ? "100+" : count + "";
    return va;
  }

  public long getCommentPrice() {
    return Long.valueOf(commentPrice.replace(",", ""));
  }

  public void setCommentPrice(String commentPrice) {
    this.commentPrice = commentPrice;
  }

  public String getTransfersAllowed() {
    long count = getCurrentMana() / getTransferPrice();
    String va = count > 100 ? "100+" : count + "";
    return va;
  }

  public long getTransferPrice() {
    return Long.valueOf(transferPrice.replace(",", ""));
  }

  public void setTransferPrice(String transferPrice) {
    this.transferPrice = transferPrice;
  }
}
