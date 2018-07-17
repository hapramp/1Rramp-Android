package com.hapramp.datamodels;

/**
 * Created by Ankit on 4/26/2018.
 */

public class FeedRenderTypeModel {

  public boolean isFirstMediaImage;
  public boolean isFirstMediaVideo;
  public boolean isTitleSet;
  public boolean hasContent;

  public String firstVideoUrl;
  public String firstImageUrl;
  public StringBuilder text;
  public String title;


  public FeedRenderTypeModel() {
    this.firstVideoUrl = null;
    this.firstImageUrl = null;
    this.text = new StringBuilder();
    this.title = null;
  }

  public void setFirstMediaImage(boolean firstMediaImage) {
    isFirstMediaImage = firstMediaImage;
  }

  public void setFirstMediaVideo(boolean firstMediaVideo) {
    isFirstMediaVideo = firstMediaVideo;
  }

  public void setFirstVideoId(String firstVideoId) {
    String url = "https://i.ytimg.com/vi/" + firstVideoId + "/hqdefault.jpg";
    this.firstVideoUrl = url;
  }

  public void setFirstImageUrl(String firstImageUrl) {
    this.firstImageUrl = firstImageUrl;
  }

  public void appendText(String text) {
    if (text.length() > 0) {
      this.text.append(text).append(" ");
      this.hasContent = true;
    }
  }

  public void setTitle(String title) {
    this.title = title;
    this.isTitleSet = true;
  }

  @Override
  public String toString() {
    return "FeedRenderTypeModel{" +
      "isFirstMediaImage=" + isFirstMediaImage +
      ", isFirstMediaVideo=" + isFirstMediaVideo +
      ", isTitleSet=" + isTitleSet +
      ", hasContent=" + hasContent +
      ", firstVideoUrl='" + firstVideoUrl + '\'' +
      ", firstImageUrl='" + firstImageUrl + '\'' +
      ", text=" + text +
      ", title='" + title + '\'' +
      '}';
  }
}
