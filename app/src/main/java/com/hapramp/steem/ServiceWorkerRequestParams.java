package com.hapramp.steem;

public class ServiceWorkerRequestParams {

  private final String lastAuthor;
  private final String lastPermlink;
  private int requestId;
  private String communityTag;
  private String subCategory;
  private String username;
  private int limit;

  public ServiceWorkerRequestParams(int requestId, String communityTag, String subCategory, String username, int limit, String lastAuthor, String lastPermlink) {
    this.requestId = requestId;
    this.communityTag = communityTag;
    this.subCategory = subCategory;
    this.username = username;
    this.limit = limit;
    this.lastAuthor = lastAuthor;
    this.lastPermlink = lastPermlink;
  }

  public boolean equals(ServiceWorkerRequestParams serviceWorkerRequestParams) {
    return requestId == serviceWorkerRequestParams.requestId;
  }

  public int getRequestId() {
    return requestId;
  }

  public String getCommunityTag() {
    return communityTag;
  }

  public String getSubCategory() {
    return subCategory;
  }

  public String getUsername() {
    return username;
  }

  public int getLimit() {
    return limit;
  }

  public String getLastAuthor() {
    return lastAuthor;
  }

  public String getLastPermlink() {
    return lastPermlink;
  }
}
