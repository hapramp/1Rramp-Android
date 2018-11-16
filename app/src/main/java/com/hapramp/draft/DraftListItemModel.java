package com.hapramp.draft;

public class DraftListItemModel {
  private String title;
  private long draftId;
  private DraftType draftType;

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

  public DraftType getDraftType() {
    return draftType;
  }

  public void setDraftType(DraftType draftType) {
    this.draftType = draftType;
  }
}
