package com.hapramp.steem;

/**
 * Created by Ankit on 4/15/2018.
 */

public class ContentCommentModel {

    public String commentAuthor;
    public String comment;
    public String createdAt;
    public String commentAuthorImageUri;

    public ContentCommentModel(String commentAuthor, String comment, String createdAt, String commentAuthorImageUri) {
        this.commentAuthor = commentAuthor;
        this.comment = comment;
        this.createdAt = createdAt;
        this.commentAuthorImageUri = commentAuthorImageUri;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCommentAuthorImageUri() {
        return commentAuthorImageUri;
    }

    @Override
    public String toString() {
        return "ContentCommentModel{" +
                "commentAuthor='" + commentAuthor + '\'' +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", commentAuthorImageUri='" + commentAuthorImageUri + '\'' +
                '}';
    }
}
