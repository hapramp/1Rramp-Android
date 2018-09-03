package com.hapramp.models;

public class CommentModel {
  private String author;
  private String permlink;
  private String category;
  private String parentAuthor;
  private String parentPermlink;
  private String body;
  private String createdAt;
  private int children;
  private String cashoutTime;
  private String totalPayoutValue;
  private String curatorPayoutValue;
  private String pendingPayoutValue;
  private String totalPendingPayoutValue;

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPermlink() {
    return permlink;
  }

  public void setPermlink(String permlink) {
    this.permlink = permlink;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getParentAuthor() {
    return parentAuthor;
  }

  public void setParentAuthor(String parentAuthor) {
    this.parentAuthor = parentAuthor;
  }

  public String getParentPermlink() {
    return parentPermlink;
  }

  public void setParentPermlink(String parentPermlink) {
    this.parentPermlink = parentPermlink;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public int getChildren() {
    return children;
  }

  public void setChildren(int children) {
    this.children = children;
  }

  public String getCashoutTime() {
    return cashoutTime;
  }

  public void setCashoutTime(String cashoutTime) {
    this.cashoutTime = cashoutTime;
  }

  public String getTotalPayoutValue() {
    return totalPayoutValue;
  }

  public void setTotalPayoutValue(String totalPayoutValue) {
    this.totalPayoutValue = totalPayoutValue;
  }

  public String getCuratorPayoutValue() {
    return curatorPayoutValue;
  }

  public void setCuratorPayoutValue(String curatorPayoutValue) {
    this.curatorPayoutValue = curatorPayoutValue;
  }

  public String getPendingPayoutValue() {
    return pendingPayoutValue;
  }

  public void setPendingPayoutValue(String pendingPayoutValue) {
    this.pendingPayoutValue = pendingPayoutValue;
  }

  public String getTotalPendingPayoutValue() {
    return totalPendingPayoutValue;
  }

  public void setTotalPendingPayoutValue(String totalPendingPayoutValue) {
    this.totalPendingPayoutValue = totalPendingPayoutValue;
  }
}
