package com.hapramp.models;

import com.hapramp.steem.models.Voter;

import java.util.ArrayList;

public class RankableCompetitionFeedItem {
  private int rank;
  private String itemId;
  private String username;
  private String createdAt;
  private ArrayList<String> tags;
  private String featuredImageLink;
  private String title;
  private String description;
  private ArrayList<Voter> voters;
  private int childrens;
  private String payout;

  public RankableCompetitionFeedItem() {
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public String getFeaturedImageLink() {
    return featuredImageLink;
  }

  public void setFeaturedImageLink(String featuredImageLink) {
    this.featuredImageLink = featuredImageLink;
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

  public ArrayList<Voter> getVoters() {
    return voters;
  }

  public void setVoters(ArrayList<Voter> voters) {
    this.voters = voters;
  }

  public int getChildrens() {
    return childrens;
  }

  public void setChildrens(int childrens) {
    this.childrens = childrens;
  }

  public String getPayout() {
    return payout;
  }

  public void setPayout(String payout) {
    this.payout = payout;
  }

  @Override
  public String toString() {
    return "RankableCompetitionFeedItem{" +
      "username='" + username + '\'' +
      ", createdAt='" + createdAt + '\'' +
      ", tags=" + tags +
      ", featuredImageLink='" + featuredImageLink + '\'' +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", voters=" + voters +
      ", childrens=" + childrens +
      ", payout='" + payout + '\'' +
      '}';
  }
}