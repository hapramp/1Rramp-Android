package com.hapramp.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 4/17/2018.
 */

public class YoutubeResultModel {

  @SerializedName("metadata")
  @Expose
  private Metadata metadata;
  @SerializedName("requestLocation")
  @Expose
  private String requestLocation;
  @SerializedName("results")
  @Expose
  private List<Result> results = null;

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public String getRequestLocation() {
    return requestLocation;
  }

  public void setRequestLocation(String requestLocation) {
    this.requestLocation = requestLocation;
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public class Metadata {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("q")
    @Expose
    private String q;

    public Integer getCount() {
      return count;
    }

    public void setCount(Integer count) {
      this.count = count;
    }

    public String getQ() {
      return q;
    }

    public void setQ(String q) {
      this.q = q;
    }

  }

  public class Result {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("get_url")
    @Expose
    private String getUrl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("stream_url")
    @Expose
    private String streamUrl;
    @SerializedName("suggest_url")
    @Expose
    private String suggestUrl;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("uploader")
    @Expose
    private String uploader;
    @SerializedName("views")
    @Expose
    private String views;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getGetUrl() {
      return getUrl;
    }

    public void setGetUrl(String getUrl) {
      this.getUrl = getUrl;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getLength() {
      return length;
    }

    public void setLength(String length) {
      this.length = length;
    }

    public String getStreamUrl() {
      return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
      this.streamUrl = streamUrl;
    }

    public String getSuggestUrl() {
      return suggestUrl;
    }

    public void setSuggestUrl(String suggestUrl) {
      this.suggestUrl = suggestUrl;
    }

    public String getThumb() {
      return thumb;
    }

    public void setThumb(String thumb) {
      this.thumb = thumb;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getUploader() {
      return uploader;
    }

    public void setUploader(String uploader) {
      this.uploader = uploader;
    }

    public String getViews() {
      return views;
    }

    public void setViews(String views) {
      this.views = views;
    }

  }

}
