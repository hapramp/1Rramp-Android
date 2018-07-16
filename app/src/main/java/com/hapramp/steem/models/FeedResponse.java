package com.hapramp.steem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 5/2/2018.
 */

public class FeedResponse {

  @Expose
  @SerializedName("posts")
  private List<Feed> feeds;

  @Expose
  @SerializedName("last_author")
  private String lastAuthor;

  @Expose
  @SerializedName("last_permlink")
  private String lastPermlink;

  public FeedResponse(List<Feed> feeds, String lastAuthor, String lastPermlink) {
    this.feeds = feeds;
    this.lastAuthor = lastAuthor;
    this.lastPermlink = lastPermlink;
  }

  public List<Feed> getFeeds() {
    return feeds;
  }

  public String getLastAuthor() {
    return lastAuthor;
  }

  public String getLastPermlink() {
    return lastPermlink;
  }
}
