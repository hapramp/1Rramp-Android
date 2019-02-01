package com.hapramp.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompetitionCreateBody {

  @Expose
  @SerializedName("prizes")
  private List<String> mPrizes;
  @Expose
  @SerializedName("communities")
  private List<Integer> mCommunities;
  @Expose
  @SerializedName("judges")
  private List<String> mJudges;
  @Expose
  @SerializedName("rules")
  private String mRules;
  @Expose
  @SerializedName("ends_at")
  private String mEndsAt;
  @Expose
  @SerializedName("starts_at")
  private String mStartsAt;
  @Expose
  @SerializedName("description")
  private String mDescription;
  @Expose
  @SerializedName("title")
  private String mTitle;
  @Expose
  @SerializedName("image")
  private String mImage;

  public CompetitionCreateBody() {
  }

  public List<String> getmPrizes() {
    return mPrizes;
  }

  public void setmPrizes(List<String> mPrizes) {
    this.mPrizes = mPrizes;
  }

  public List<Integer> getmCommunities() {
    return mCommunities;
  }

  public void setmCommunities(List<Integer> mCommunities) {
    this.mCommunities = mCommunities;
  }

  public List<String> getmJudges() {
    return mJudges;
  }

  public void setmJudges(List<String> mJudges) {
    this.mJudges = mJudges;
  }

  public String getmRules() {
    return mRules;
  }

  public void setmRules(String mRules) {
    this.mRules = mRules;
  }

  public String getmEndsAt() {
    return mEndsAt;
  }

  public void setmEndsAt(String mEndsAt) {
    this.mEndsAt = mEndsAt;
  }

  public String getmStartsAt() {
    return mStartsAt;
  }

  public void setmStartsAt(String mStartsAt) {
    this.mStartsAt = mStartsAt;
  }

  public String getmDescription() {
    return mDescription;
  }

  public void setmDescription(String mDescription) {
    this.mDescription = mDescription;
  }

  public String getmTitle() {
    return mTitle;
  }

  public void setmTitle(String mTitle) {
    this.mTitle = mTitle;
  }

  public String getmImage() {
    return mImage;
  }

  public void setmImage(String mImage) {
    this.mImage = mImage;
  }

  @Override
  public String toString() {
    return "CompetitionCreateBody{" +
      "mPrizes=" + mPrizes +
      ", mCommunities=" + mCommunities +
      ", mJudges=" + mJudges +
      ", mRules='" + mRules + '\'' +
      ", mEndsAt='" + mEndsAt + '\'' +
      ", mStartsAt='" + mStartsAt + '\'' +
      ", mDescription='" + mDescription + '\'' +
      ", mTitle='" + mTitle + '\'' +
      ", mImage='" + mImage + '\'' +
      '}';
  }
}
