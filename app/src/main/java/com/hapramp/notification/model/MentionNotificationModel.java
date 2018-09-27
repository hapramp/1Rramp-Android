package com.hapramp.notification.model;

import com.google.gson.Gson;

public class MentionNotificationModel extends BaseNotificationModel {
  public String type;
  public boolean isRootPost;
  public String author;
  public String parentPermlink;
  public String permlink;
  public String timestamp;

  public MentionNotificationModel(String type, boolean isRootPost, String author, String parentPermlink, String permlink, String timestamp) {
    this.type = type;
    this.isRootPost = isRootPost;
    this.author = author;
    this.parentPermlink = parentPermlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public String getParentPermlink() {
    return parentPermlink;
  }

  public void setParentPermlink(String parentPermlink) {
    this.parentPermlink = parentPermlink;
  }

  public MentionNotificationModel(String type, String notificationId, String type1, boolean isRootPost, String author, String parentPermlink, String permlink, String timestamp) {
    super(type, notificationId);
    this.type = type1;
    this.isRootPost = isRootPost;
    this.author = author;
    this.parentPermlink = parentPermlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public MentionNotificationModel(String type, String type1, boolean isRootPost, String author, String parentPermlink, String permlink, String timestamp) {
    super(type);
    this.type = type1;
    this.isRootPost = isRootPost;
    this.author = author;
    this.parentPermlink = parentPermlink;
    this.permlink = permlink;
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isRootPost() {
    return isRootPost;
  }

  public void setRootPost(boolean rootPost) {
    isRootPost = rootPost;
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

}
