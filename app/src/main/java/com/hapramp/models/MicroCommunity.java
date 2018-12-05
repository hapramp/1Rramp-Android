package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.ParentMicroCommunity;

public class MicroCommunity implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<MicroCommunity> CREATOR = new Parcelable.Creator<MicroCommunity>() {
    @Override
    public MicroCommunity createFromParcel(Parcel in) {
      return new MicroCommunity(in);
    }

    @Override
    public MicroCommunity[] newArray(int size) {
      return new MicroCommunity[size];
    }
  };
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("tag")
  @Expose
  private String tag;
  @SerializedName("image_url")
  @Expose
  private String imageUrl;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("community")
  @Expose
  private ParentMicroCommunity community;

  protected MicroCommunity(Parcel in) {
    id = in.readByte() == 0x00 ? null : in.readInt();
    name = in.readString();
    tag = in.readString();
    imageUrl = in.readString();
    description = in.readString();
    community = (ParentMicroCommunity) in.readValue(ParentMicroCommunity.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeInt(id);
    }
    dest.writeString(name);
    dest.writeString(tag);
    dest.writeString(imageUrl);
    dest.writeString(description);
    dest.writeValue(community);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ParentMicroCommunity getCommunity() {
    return community;
  }

  public void setCommunity(ParentMicroCommunity community) {
    this.community = community;
  }
}
