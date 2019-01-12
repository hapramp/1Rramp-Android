package com.hapramp.notification.model;

public class ContestWinnerDeclaredNotificationModel extends BaseNotificationModel{
  private String title;
  private String id;
  private String type;
  private String image;

  public ContestWinnerDeclaredNotificationModel(String title, String id, String type, String image) {
    this.title = title;
    this.id = id;
    this.type = type;
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
