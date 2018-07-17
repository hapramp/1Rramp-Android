package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsResponse {


  @SerializedName("start")
  public int start;
  @SerializedName("limit")
  public int limit;
  @SerializedName("count")
  public int count;
  @SerializedName("next")
  public String next;
  @SerializedName("previous")
  public String previous;
  @SerializedName("results")
  public List<Results> results;

  public CommentsResponse(int start, int limit, int count, String next, String previous, List<Results> results) {
    this.start = start;
    this.limit = limit;
    this.count = count;
    this.next = next;
    this.previous = previous;
    this.results = results;
  }

  @Override
  public String toString() {
    return "CommentsResponse{" +
      "start=" + start +
      ", limit=" + limit +
      ", count=" + count +
      ", next='" + next + '\'' +
      ", previous='" + previous + '\'' +
      ", results=" + results +
      '}';
  }

  public static class User {
    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("image_uri")
    public String image_uri;

    public User(int id, String username, String full_name, String image_uri) {
      this.id = id;
      this.username = username;
      this.full_name = full_name;
      this.image_uri = image_uri;
    }
  }

  public static class Results {
    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("content")
    public String content;
    @SerializedName("is_voted")
    public boolean is_voted;
    @SerializedName("vote_count")
    public int vote_count;
    @SerializedName("user")
    public User user;

    public Results(int id, String created_at, String content, boolean is_voted, int vote_count, User user) {
      this.id = id;
      this.created_at = created_at;
      this.content = content;
      this.is_voted = is_voted;
      this.vote_count = vote_count;
      this.user = user;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getCreated_at() {
      return created_at;
    }

    public void setCreated_at(String created_at) {
      this.created_at = created_at;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public boolean isIs_voted() {
      return is_voted;
    }

    public void setIs_voted(boolean is_voted) {
      this.is_voted = is_voted;
    }

    public int getVote_count() {
      return vote_count;
    }

    public void setVote_count(int vote_count) {
      this.vote_count = vote_count;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }
  }
}
