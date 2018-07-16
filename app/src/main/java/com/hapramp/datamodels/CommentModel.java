package com.hapramp.datamodels;

/**
 * Created by Ankit on 12/13/2017.
 */

public class CommentModel {

  public String postPermlink;
  public String commentId;
  public String userId;
  public String userDpUrl;
  public String userName;
  public String comment;
  public String commentTime;

  public CommentModel(String commentId, String userId, String userDpUrl, String userName, String comment, String commentTime) {
    this.commentId = commentId;
    this.userId = userId;
    this.userDpUrl = userDpUrl;
    this.userName = userName;
    this.comment = comment;
    this.commentTime = commentTime;
  }

  public String getPostPermlink() {
    return postPermlink;
  }

  public void setPostPermlink(String postPermlink) {
    this.postPermlink = postPermlink;
  }

  public String getCommentId() {
    return commentId;
  }

  public void setCommentId(String commentId) {
    this.commentId = commentId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserDpUrl() {
    return userDpUrl;
  }

  public void setUserDpUrl(String userDpUrl) {
    this.userDpUrl = userDpUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getCommentTime() {
    return commentTime;
  }

  public void setCommentTime(String commentTime) {
    this.commentTime = commentTime;
  }

  @Override
  public String toString() {
    return "CommentModel{" +
      "commentId='" + commentId + '\'' +
      ", userId='" + userId + '\'' +
      ", userDpUrl='" + userDpUrl + '\'' +
      ", userName='" + userName + '\'' +
      ", comment='" + comment + '\'' +
      ", commentTime='" + commentTime + '\'' +
      '}';
  }
}
