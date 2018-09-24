package com.hapramp.analytics;

public class EventReportModel {
  public String openTime;
  public String closeTime;
  public String eventSequence;

  public EventReportModel(String openTime, String closeTime, String eventSequence) {
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.eventSequence = eventSequence;
  }
}
