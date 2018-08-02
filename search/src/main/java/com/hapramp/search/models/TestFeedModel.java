package com.hapramp.search.models;

public class TestFeedModel {
  private String username;
  private String body;
  private String title;
  private String time;

  public TestFeedModel(String username, String body, String title, String time) {
    this.username = username;
    this.body = body;
    this.title = title;
    this.time = time;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
