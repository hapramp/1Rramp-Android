package com.hapramp.notification.model;

public class ReblogNotificationModel  extends BaseNotificationModel {
  public String type;
  public String account;
  public String permlink;
  public String timestamp;
  public String parentPermlink;

  public ReblogNotificationModel(String type, String account, String permlink, String timestamp, String parentPermlink) {
    this.type = type;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public String getParentPermlink() {
    return parentPermlink;
  }

  public void setParentPermlink(String parentPermlink) {
    this.parentPermlink = parentPermlink;
  }

  public ReblogNotificationModel(String type, String notificationId, String type1, String account, String permlink, String timestamp, String parentPermlink) {
    super(type, notificationId);
    this.type = type1;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public ReblogNotificationModel(String type, String type1, String account, String permlink, String timestamp, String parentPermlink) {
    super(type);
    this.type = type1;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parentPermlink = parentPermlink;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPermlink() {
    return permlink;
  }

  public void setPermlink(String permlink) {
    this.permlink = permlink;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "ReblogNotificationModel{" +
      "type='" + type + '\'' +
      ", account='" + account + '\'' +
      ", permlink='" + permlink + '\'' +
      ", timestamp='" + timestamp + '\'' +
      ", parentPermlink='" + parentPermlink + '\'' +
      '}';
  }
}
