package com.hapramp.notification.model;

public class ContestStartedNotificationModel extends BaseNotificationModel{
  private String title;
  private String image;
  private String type;
  private String id;
  private String tag;


  public ContestStartedNotificationModel(String title, String image, String type, String id, String tag) {
    this.title = title;
    this.image = image;
    this.type = type;
    this.id = id;
    this.tag = tag;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
