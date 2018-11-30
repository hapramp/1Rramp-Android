package com.hapramp.notification.model;

public class CompetitionResultNotificationModel extends BaseNotificationModel {
  private String type;
  private String title;
  private String compId;
  private String competitionTitle;
  private String description;

  public CompetitionResultNotificationModel(String type, String title, String compId, String competitionTitle, String description) {
    this.type = type;
    this.title = title;
    this.compId = compId;
    this.competitionTitle = competitionTitle;
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

  public String getCompetitionTitle() {
    return competitionTitle;
  }

  public void setCompetitionTitle(String competitionTitle) {
    this.competitionTitle = competitionTitle;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCompetitionId() {
    return compId;
  }

  public void setCompId(String compId) {
    this.compId = compId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
