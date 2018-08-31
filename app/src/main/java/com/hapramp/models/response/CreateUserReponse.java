package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/21/2017.
 */

public class CreateUserReponse {


  @SerializedName("id")
  public int id;
  @SerializedName("email")
  public String email;
  @SerializedName("username")
  public String username;
  @SerializedName("organization")
  public Organization organization;
  @SerializedName("full_name")
  public String full_name;
  @SerializedName("skills")
  public List<Skills> skills;
  @SerializedName("karma")
  public int karma;

  public CreateUserReponse(int id, String email, String username, Organization organization, String full_name, List<Skills> skills, int karma) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.organization = organization;
    this.full_name = full_name;
    this.skills = skills;
    this.karma = karma;
  }

  public static class Organization {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("username")
    public String username;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("description")
    public String description;

    @Override
    public String toString() {
      return "Organization{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", username='" + username + '\'' +
        ", image_uri='" + image_uri + '\'' +
        ", description='" + description + '\'' +
        '}';
    }

  }

  public static class Skills {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("description")
    public String description;

    @Override
    public String toString() {
      return "Skills{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", image_uri='" + image_uri + '\'' +
        ", description='" + description + '\'' +
        '}';
    }

  }

  @Override
  public String toString() {
    return "CreateUserReponse{" +
      "id=" + id +
      ", email='" + email + '\'' +
      ", username='" + username + '\'' +
      ", organization=" + organization +
      ", full_name='" + full_name + '\'' +
      ", skills=" + skills +
      ", karma=" + karma +
      '}';
  }
}
