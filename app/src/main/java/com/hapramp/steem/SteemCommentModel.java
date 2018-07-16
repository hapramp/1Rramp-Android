package com.hapramp.steem;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ankit on 4/15/2018.
 */

@Entity(tableName = "comments")
public class SteemCommentModel implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<SteemCommentModel> CREATOR = new Parcelable.Creator<SteemCommentModel>() {
    @Override
    public SteemCommentModel createFromParcel(Parcel in) {
      return new SteemCommentModel(in);
    }

    @Override
    public SteemCommentModel[] newArray(int size) {
      return new SteemCommentModel[size];
    }
  };
  @PrimaryKey(autoGenerate = true)
  public int id = 0;
  public String postPermlink;
  public String commentAuthor;
  public String comment;
  public String createdAt;
  public String commentAuthorImageUri;

  protected SteemCommentModel(Parcel in) {
    commentAuthor = in.readString();
    comment = in.readString();
    createdAt = in.readString();
    commentAuthorImageUri = in.readString();
  }

  public SteemCommentModel(String commentAuthor, String comment, String createdAt, String commentAuthorImageUri) {
    this.commentAuthor = commentAuthor;
    this.comment = comment;
    this.createdAt = createdAt;
    this.commentAuthorImageUri = commentAuthorImageUri;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(commentAuthor);
    dest.writeString(comment);
    dest.writeString(createdAt);
    dest.writeString(commentAuthorImageUri);
  }

  public String getPostPermlink() {
    return postPermlink;
  }

  public void setPostPermlink(String postPermlink) {
    this.postPermlink = postPermlink;
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
