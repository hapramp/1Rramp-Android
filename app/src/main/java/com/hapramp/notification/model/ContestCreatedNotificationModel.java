package com.hapramp.notification.model;

public class ContestCreatedNotificationModel extends BaseNotificationModel{
  private String startsAt;
  private String description;
  private String endsAt;
  private String[] prizes;
  private String id;
  private String tag;
  private String type;
  private String image;
  private String title;

  public ContestCreatedNotificationModel(String startsAt, String description, String endsAt, String[] prizes, String id, String tag, String type, String image, String title) {
    this.startsAt = startsAt;
    this.description = description;
    this.endsAt = endsAt;
    this.prizes = prizes;
    this.id = id;
    this.tag = tag;
    this.type = type;
    this.image = image;
    this.title = title;
  }

  public String getStartsAt() {
    return startsAt;
  }

  public void setStartsAt(String startsAt) {
    this.startsAt = startsAt;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(String endsAt) {
    this.endsAt = endsAt;
  }

  public String[] getPrizes() {
    return prizes;
  }

  public void setPrizes(String[] prizes) {
    this.prizes = prizes;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
