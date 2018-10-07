package com.hapramp.notification.model;


public class ReplyNotificationModel  extends BaseNotificationModel {
  public String type;
  public String parent_permlink;
  public String author;
  public String permlink;
  public String timestamp;

  public ReplyNotificationModel(String type, String parent_permlink, String author, String permlink, String timestamp) {
    this.type = type;
    this.parent_permlink = parent_permlink;
    this.author = author;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getParent_permlink() {
    return parent_permlink;
  }

  public void setParent_permlink(String parent_permlink) {
    this.parent_permlink = parent_permlink;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
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
    return "ReplyNotificationModel{" +
      "type='" + type + '\'' +
      ", parent_permlink='" + parent_permlink + '\'' +
      ", author='" + author + '\'' +
      ", permlink='" + permlink + '\'' +
      ", timestamp='" + timestamp + '\'' +
      '}';
  }
}
