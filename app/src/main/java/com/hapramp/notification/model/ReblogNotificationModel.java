package com.hapramp.notification.model;

public class ReblogNotificationModel  extends BaseNotificationModel {
  public String type;
  public String account;
  public String permlink;
  public String timestamp;
  public String parent_permlink;

  public ReblogNotificationModel(String type, String account, String permlink, String timestamp, String parent_permlink) {
    this.type = type;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parent_permlink = parent_permlink;
  }

  public String getParentPermlink() {
    return parent_permlink;
  }

  public void setParentPermlink(String parent_permlink) {
    this.parent_permlink = parent_permlink;
  }

  public ReblogNotificationModel(String type, String notificationId, String type1, String account, String permlink, String timestamp, String parent_permlink) {
    super(type, notificationId);
    this.type = type1;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parent_permlink = parent_permlink;
  }

  public ReblogNotificationModel(String type, String type1, String account, String permlink, String timestamp, String parent_permlink) {
    super(type);
    this.type = type1;
    this.account = account;
    this.permlink = permlink;
    this.timestamp = timestamp;
    this.parent_permlink = parent_permlink;
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
      ", parent_permlink='" + parent_permlink + '\'' +
      '}';
  }
}
