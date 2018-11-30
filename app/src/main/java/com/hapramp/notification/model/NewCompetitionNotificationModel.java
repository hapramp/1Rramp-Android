package com.hapramp.notification.model;

public class NewCompetitionNotificationModel extends BaseNotificationModel {
  public String type;
  private String title;
  private String description;

  public NewCompetitionNotificationModel(String type, String title, String description) {
    this.type = type;
    this.title = title;
    this.description = description;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "NewCompetitionNotificationModel{" +
      "title='" + title + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
