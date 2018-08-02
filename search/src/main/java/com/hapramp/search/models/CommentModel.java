package com.hapramp.search.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentModel {

  @Expose
  @SerializedName("result")
  private Result result;

  public Result getResult() {
    return result;
  }

  public static class Result {
    @Expose
    @SerializedName("discussions")
    private List<Discussions> discussions;

    public List<Discussions> getDiscussions() {
      return discussions;
    }
  }

  public static class Discussions {
    @Expose
    @SerializedName("json_metadata")
    private String json_metadata;
    @Expose
    @SerializedName("body")
    private String body;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("parent_permlink")
    private String parent_permlink;
    @Expose
    @SerializedName("children")
    private int children;
    @Expose
    @SerializedName("parent_author")
    private String parent_author;
    @Expose
    @SerializedName("category")
    private String category;
    @Expose
    @SerializedName("permlink")
    private String permlink;
    @Expose
    @SerializedName("author")
    private String author;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("last_update")
    private String last_update;
    @Expose
    @SerializedName("created")
    private String created;

    public Discussions(String json_metadata, String body, String title, String parent_permlink, int children, String parent_author, String category, String permlink, String author, int id, String last_update, String created) {
      this.json_metadata = json_metadata;
      this.body = body;
      this.title = title;
      this.parent_permlink = parent_permlink;
      this.children = children;
      this.parent_author = parent_author;
      this.category = category;
      this.permlink = permlink;
      this.author = author;
      this.id = id;
      this.last_update = last_update;
      this.created = created;
    }

    public String getJson_metadata() {
      return json_metadata;
    }

    public String getBody() {
      return body;
    }

    public String getTitle() {
      return title;
    }

    public int getChildren() {
      return children;
    }

    public void setChildren(int children) {
      this.children = children;
    }

    public String getParent_permlink() {
      return parent_permlink;
    }

    public String getParent_author() {
      return parent_author;
    }

    public String getCategory() {
      return category;
    }

    public String getPermlink() {
      return permlink;
    }

    public String getAuthor() {
      return author;
    }

    public int getId() {
      return id;
    }

    public String getLast_update() {
      return last_update;
    }

    public String getCreated() {
      return created;
    }
  }
}
