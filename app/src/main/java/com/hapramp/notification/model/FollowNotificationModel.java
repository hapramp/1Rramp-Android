package com.hapramp.notification.model;

public class FollowNotificationModel extends BaseNotificationModel {
  public String type;
  public String follower;
  public String timestamp;

  public FollowNotificationModel(String type, String follower, String timestamp) {
    this.type = type;
    this.follower = follower;
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFollower() {
    return follower;
  }

  public void setFollower(String follower) {
    this.follower = follower;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
