package com.hapramp.steem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ankit on 3/8/2018.
 */

public class Feed implements Parcelable {
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
    @Override
    public Feed createFromParcel(Parcel in) {
      return new Feed(in);
    }

    @Override
    public Feed[] newArray(int size) {
      return new Feed[size];
    }
  };
  private String author;
  private String permlink;
  private String category;
  private String parentAuthor;
  private String parentPermlink;
  private String title = "";
  private String body;
  private String cleanedBody;
  private String featuredImageUrl = "";
  private String format;
  private ArrayList<String> tags;
  private String createdAt;
  private int depth;
  private int children;
  private String totalPayoutValue;
  private String curatorPayoutValue;
  private String rootAuthor;
  private String rootPermlink;
  private String url;
  private String pendingPayoutValue;
  private String totalPendingPayoutValue;
  private String cashOutTime;
  private ArrayList<Voter> voters;
  private String authorReputation;

  @Override
  public int describeContents() {
    return 0;
  }

  protected Feed(Parcel in) {
    author = in.readString();
    permlink = in.readString();
    category = in.readString();
    parentAuthor = in.readString();
    parentPermlink = in.readString();
    title = in.readString();
    body = in.readString();
    cleanedBody = in.readString();
    featuredImageUrl = in.readString();
    format = in.readString();
    if (in.readByte() == 0x01) {
      tags = new ArrayList<String>();
      in.readList(tags, String.class.getClassLoader());
    } else {
      tags = null;
    }
    createdAt = in.readString();
    depth = in.readInt();
    children = in.readInt();
    totalPayoutValue = in.readString();
    curatorPayoutValue = in.readString();
    rootAuthor = in.readString();
    rootPermlink = in.readString();
    url = in.readString();
    pendingPayoutValue = in.readString();
    totalPendingPayoutValue = in.readString();
    cashOutTime = in.readString();
    if (in.readByte() == 0x01) {
      voters = new ArrayList<Voter>();
      in.readList(voters, Voter.class.getClassLoader());
    } else {
      voters = null;
    }
    authorReputation = in.readString();
  }

  public Feed() {
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(author);
    dest.writeString(permlink);
    dest.writeString(category);
    dest.writeString(parentAuthor);
    dest.writeString(parentPermlink);
    dest.writeString(title);
    dest.writeString(body);
    dest.writeString(cleanedBody);
    dest.writeString(featuredImageUrl);
    dest.writeString(format);
    if (tags == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(tags);
    }
    dest.writeString(createdAt);
    dest.writeInt(depth);
    dest.writeInt(children);
    dest.writeString(totalPayoutValue);
    dest.writeString(curatorPayoutValue);
    dest.writeString(rootAuthor);
    dest.writeString(rootPermlink);
    dest.writeString(url);
    dest.writeString(pendingPayoutValue);
    dest.writeString(totalPendingPayoutValue);
    dest.writeString(cashOutTime);
    if (voters == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(voters);
    }
    dest.writeString(authorReputation);
  }

  public String getCleanedBody() {
    return cleanedBody;
  }

  public void setCleanedBody(String cleanedBody) {
    this.cleanedBody = cleanedBody;
  }

  public String getCashOutTime() {
    return cashOutTime;
  }

  public void setCashOutTime(String cashOutTime) {
    this.cashOutTime = cashOutTime;
  }

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getFeaturedImageUrl() {
    return featuredImageUrl;
  }

  public void setFeaturedImageUrl(String featuredImageUrl) {
    this.featuredImageUrl = featuredImageUrl;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public int getChildren() {
    return children;
  }

  public void setChildren(int children) {
    this.children = children;
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

  public String getRootAuthor() {
    return rootAuthor;
  }

  public void setRootAuthor(String rootAuthor) {
    this.rootAuthor = rootAuthor;
  }

  public String getRootPermlink() {
    return rootPermlink;
  }

  public void setRootPermlink(String rootPermlink) {
    this.rootPermlink = rootPermlink;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public ArrayList<Voter> getVoters() {
    return voters;
  }

  public void setVoters(ArrayList<Voter> voters) {
    this.voters = voters;
  }

  public String getAuthorReputation() {
    return authorReputation;
  }

  public void setAuthorReputation(String authorReputation) {
    this.authorReputation = authorReputation;
  }

  @Override
  public String toString() {
    return "Feed{" +
      "author='" + author + '\'' +
      ", permlink='" + permlink + '\'' +
      ", category='" + category + '\'' +
      ", parentAuthor='" + parentAuthor + '\'' +
      ", parentPermlink='" + parentPermlink + '\'' +
      ", title='" + title + '\'' +
      ", body='" + body + '\'' +
      ", cleanedBody='" + cleanedBody + '\'' +
      ", featuredImageUrl='" + featuredImageUrl + '\'' +
      ", format='" + format + '\'' +
      ", tags=" + tags +
      ", createdAt='" + createdAt + '\'' +
      ", depth=" + depth +
      ", children=" + children +
      ", totalPayoutValue='" + totalPayoutValue + '\'' +
      ", curatorPayoutValue='" + curatorPayoutValue + '\'' +
      ", rootAuthor='" + rootAuthor + '\'' +
      ", rootPermlink='" + rootPermlink + '\'' +
      ", url='" + url + '\'' +
      ", pendingPayoutValue='" + pendingPayoutValue + '\'' +
      ", totalPendingPayoutValue='" + totalPendingPayoutValue + '\'' +
      ", voters=" + voters +
      ", authorReputation='" + authorReputation + '\'' +
      '}';
  }
}
