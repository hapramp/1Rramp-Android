package com.hapramp.draft;

public class DraftListItemModel {
  private String title;
  private int draftId;
  private String draftType;
  private String json;
  private String lastModified;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getLastModified() {
    return lastModified;
  }

  public void setLastModified(String lastModified) {
    this.lastModified = lastModified;
  }

  public long getDraftId() {
    return draftId;
  }

  public void setDraftId(int draftId) {
    this.draftId = draftId;
  }

  public String getDraftType() {
    return draftType;
  }

  public void setDraftType(String draftType) {
    this.draftType = draftType;
  }
}
