package com.hapramp.steem.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 3/27/2018.
 */


public class Profile {

  private static final String TAG = Profile.class.getSimpleName();

  @SerializedName("profile_image")
  @Expose
  public String profileImage = "";
  @SerializedName("name")
  @Expose
  public String name = "";
  @SerializedName("location")
  public String location = "";
  @Expose
  @SerializedName("cover_image")
  public String cover_image = "";
  @Expose
  @SerializedName("website")
  public String website = "";
  @SerializedName("about")
  @Expose
  public String about = "";

  public Profile(String profileImage, String name, String location, String website, String about, String cover_image) {
    this.profileImage = profileImage;
    this.name = name;
    this.location = location;
    this.website = website;
    this.cover_image = cover_image;
    this.about = about;
  }

  public static String getDefaultProfileAsJson() {
    String json = "{\"about\":\"about\",\"location\":\"location\",\"name\":\"name\",\"profile_image\":\"https://user-images.githubusercontent.com/10809719/38206885-b36c8a66-36c9-11e8-9c7a-3bba603b4994.png\",\"website\":\"website\",\"cover_image\":\"cover_image_url\"}";
    return json;
  }

  public String getCover_image() {
    return cover_image;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }

  public String getWebsite() {
    return website;
  }

  public String getAbout() {
    return about;
  }


}
