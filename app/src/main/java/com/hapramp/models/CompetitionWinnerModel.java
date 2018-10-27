package com.hapramp.models;

public class CompetitionWinnerModel {
  private int rank;
  private String id;
  private String imageUrl;
  private String permlink;
  private String title;
  private String username;

  public CompetitionWinnerModel() {
  }

  public String getPermlink() {
    return permlink;
  }

  public void setPermlink(String permlink) {
    this.permlink = permlink;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
