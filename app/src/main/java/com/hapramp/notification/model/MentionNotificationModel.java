package com.hapramp.notification.model;


public class MentionNotificationModel extends BaseNotificationModel {
  public String type;
  public boolean is_root_post;
  public String author;
  public String parent_permlink;
  public String permlink;
  public String timestamp;

  public MentionNotificationModel(String type, boolean isRootPost, String author, String parent_permlink, String permlink, String timestamp) {
    this.type = type;
    this.is_root_post = isRootPost;
    this.author = author;
    this.parent_permlink = parent_permlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public MentionNotificationModel(String type, String notificationId, String type1, boolean isRootPost, String author, String parent_permlink, String permlink, String timestamp) {
    super(type, notificationId);
    this.type = type1;
    this.is_root_post = isRootPost;
    this.author = author;
    this.parent_permlink = parent_permlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public MentionNotificationModel(String type, String type1, boolean isRootPost, String author, String parent_permlink, String permlink, String timestamp) {
    super(type);
    this.type = type1;
    this.is_root_post = isRootPost;
    this.author = author;
    this.parent_permlink = parent_permlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  public boolean isIs_root_post() {
    return is_root_post;
  }

  public void setIs_root_post(boolean is_root_post) {
    this.is_root_post = is_root_post;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getParent_permlink() {
    return parent_permlink;
  }

  public void setParent_permlink(String parent_permlink) {
    this.parent_permlink = parent_permlink;
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
}
