package com.hapramp.draft;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ShortPostDraftModel {
  @SerializedName("draftId")
  private long draftId;
  @SerializedName("image_url")
  private String postImageUrl;
  @SerializedName("title")
  private String title;
  @SerializedName("text")
  private String text;
  @SerializedName("mCommunitySelection")
  private List<String> communities = new ArrayList<>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getDraftId() {
    return draftId;
  }

  public void setDraftId(long draftId) {
    this.draftId = draftId;
  }

  public String getPostImageUrl() {
    return postImageUrl;
  }

  public void setPostImageUrl(String postImageUrl) {
    this.postImageUrl = postImageUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<String> getCommunities() {
    return communities;
  }

  public void setCommunities(List<String> communities) {
    this.communities = communities;
  }

  @Override
  public String toString() {
    return "ShortPostDraftModel{" +
      "draftId=" + draftId +
      ", postImageUrl='" + postImageUrl + '\'' +
      ", title='" + title + '\'' +
      ", text='" + text + '\'' +
      ", communities=" + communities +
      '}';
  }
}
