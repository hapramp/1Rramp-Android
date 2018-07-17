package com.hapramp.steem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/14/2018.
 */

public class FeedWrapper {

  @Expose
  @SerializedName("post")
  public Feed feed;

  public FeedWrapper(Feed feed) {
    this.feed = feed;
  }

  public Feed getFeed() {
    return feed;
  }

  public void setFeed(Feed feed) {
    this.feed = feed;
  }
}
