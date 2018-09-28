package com.hapramp.notification.model;


public class VoteNotificationModel extends BaseNotificationModel {
  public String type;
  public String voter;
  public String permlink;
  public String weight;
  public String timestamp;
  public String parentPermlink;

  public VoteNotificationModel(String type, String voter, String permlink, String weight, String timestamp, String parentPermlink) {
    this.type = type;
    this.voter = voter;
    this.permlink = permlink;
    this.weight = weight;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public String getParentPermlink() {
    return parentPermlink;
  }

  public void setParentPermlink(String parentPermlink) {
    this.parentPermlink = parentPermlink;
  }

  public VoteNotificationModel(String type, String notificationId, String type1, String voter, String permlink, String weight, String timestamp, String parentPermlink) {
    super(type, notificationId);
    this.type = type1;
    this.voter = voter;
    this.permlink = permlink;
    this.weight = weight;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public VoteNotificationModel(String type, String type1, String voter, String permlink, String weight, String timestamp, String parentPermlink) {
    super(type);
    this.type = type1;
    this.voter = voter;
    this.permlink = permlink;
    this.weight = weight;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public String getType() {
    return type;
  }

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
