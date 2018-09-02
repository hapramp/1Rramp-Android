package com.hapramp.steem.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.LocalConfig;

import java.util.List;

/**
 * Created by Ankit on 4/9/2018.
 */

public class JsonMetadata {

  @Expose
  @SerializedName("tags")
  public List<String> tags;

  @Expose
  @SerializedName("app")
  public String app;

  @Expose
  @SerializedName("image")
  public List<String> images;

  @Expose
  @SerializedName("format")
  public String format;

  public JsonMetadata(List<String> tags, List<String> images) {
    this.tags = tags;
    this.app = LocalConfig.APP_TAG;
    this.format = "markdown";
    this.images = images;
  }
  public String getJson() {
    return new Gson().toJson(this, JsonMetadata.class);
  }
}
