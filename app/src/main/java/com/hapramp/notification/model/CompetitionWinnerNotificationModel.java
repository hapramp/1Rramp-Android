package com.hapramp.notification.model;

public class CompetitionWinnerNotificationModel extends BaseNotificationModel {
  private String type;
  private String title;
  private String competitionId;
  private String competitionTitle;
  private String description;

  public CompetitionWinnerNotificationModel(String type, String title, String competitionId, String competitionTitle, String description) {
    this.type = type;
    this.title = title;
    this.competitionId = competitionId;
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

  public String getCompetitionId() {
    return competitionId;
  }

  public void setCompetitionId(String competitionId) {
    this.competitionId = competitionId;
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
}
