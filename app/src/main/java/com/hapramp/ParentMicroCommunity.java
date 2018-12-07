package com.hapramp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentMicroCommunity implements Parcelable {
  @SuppressWarnings("unused")
  public static final Parcelable.Creator<ParentMicroCommunity> CREATOR = new Parcelable.Creator<ParentMicroCommunity>() {
    @Override
    public ParentMicroCommunity createFromParcel(Parcel in) {
      return new ParentMicroCommunity(in);
    }

    @Override
    public ParentMicroCommunity[] newArray(int size) {
      return new ParentMicroCommunity[size];
    }
  };
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("color")
  @Expose
  private String color;
  @SerializedName("tag")
  @Expose
  private String tag;
  @SerializedName("image_uri")
  @Expose
  private String imageUri;
  @SerializedName("description")
  @Expose
  private String description;

  protected ParentMicroCommunity(Parcel in) {
    id = in.readByte() == 0x00 ? null : in.readInt();
    name = in.readString();
    color = in.readString();
    tag = in.readString();
    imageUri = in.readString();
    description = in.readString();
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
    dest.writeString(color);
    dest.writeString(tag);
    dest.writeString(imageUri);
    dest.writeString(description);
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

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getImageUri() {
    return imageUri;
  }

  public void setImageUri(String imageUri) {
    this.imageUri = imageUri;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
