package com.hapramp.notification.model;


public class VoteNotificationModel extends BaseNotificationModel {
  public String type;
  public String voter;
  public String permlink;
  public String weight;
  public String timestamp;
  public String parent_permlink;

  public VoteNotificationModel(String type, String voter, String permlink, String weight, String timestamp, String parentPermlink) {
    this.type = type;
    this.voter = voter;
    this.permlink = permlink;
    this.weight = weight;
    this.timestamp = timestamp;
    this.parent_permlink = parentPermlink;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  public String getVoter() {
    return voter;
  }

  public void setVoter(String voter) {
    this.voter = voter;
  }

  public String getPermlink() {
    return permlink;
  }

  public void setPermlink(String permlink) {
    this.permlink = permlink;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getParent_permlink() {
    return parent_permlink;
  }

  public void setParent_permlink(String parent_permlink) {
    this.parent_permlink = parent_permlink;
  }

  @Override
  public String toString() {
    return "VoteNotificationModel{" +
      "type='" + type + '\'' +
      ", voter='" + voter + '\'' +
      ", permlink='" + permlink + '\'' +
      ", weight='" + weight + '\'' +
      ", timestamp='" + timestamp + '\'' +
      '}';
  }
}
