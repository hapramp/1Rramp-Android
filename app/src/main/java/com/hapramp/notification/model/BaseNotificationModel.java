package com.hapramp.notification.model;

public class BaseNotificationModel {
  public String type;
  public boolean read;
  public String notificationId;

  public BaseNotificationModel() {
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public BaseNotificationModel(String type, String notificationId) {
    this.type = type;
    this.notificationId = notificationId;
  }

  public BaseNotificationModel(String type) {
    this.type = type;
  }

  public String getNotificationId() {
    return this.notificationId;
  }

  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
