package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CompetitionModel implements Parcelable {
  private String mParticipationHashtag;
  private int mPostCount;
  private int mParticipantCount;
  private List<JudgeModel> mJudges;
  private String mRules;
  private String mEndsAt;
  private String mStartsAt;
  private String mDescription;
  private String mTitle;
  private String mImage;
  private String mCreatedAt;
  private CompetitionAdmin mAdmin;
  private String mId;
  private List<CommunityModel> communities;
  private List<String> prizes;
  private boolean winners_announced;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mParticipationHashtag);
    dest.writeInt(this.mPostCount);
    dest.writeInt(this.mParticipantCount);
    dest.writeTypedList(this.mJudges);
    dest.writeString(this.mRules);
    dest.writeString(this.mEndsAt);
    dest.writeString(this.mStartsAt);
    dest.writeString(this.mDescription);
    dest.writeString(this.mTitle);
    dest.writeString(this.mImage);
    dest.writeString(this.mCreatedAt);
    dest.writeParcelable(this.mAdmin, flags);
    dest.writeString(this.mId);
    dest.writeTypedList(this.communities);
    dest.writeStringList(this.prizes);
    dest.writeByte(this.winners_announced ? (byte) 1 : (byte) 0);
  }

  public CompetitionModel() {
  }

  protected CompetitionModel(Parcel in) {
    this.mParticipationHashtag = in.readString();
    this.mPostCount = in.readInt();
    this.mParticipantCount = in.readInt();
    this.mJudges = in.createTypedArrayList(JudgeModel.CREATOR);
    this.mRules = in.readString();
    this.mEndsAt = in.readString();
    this.mStartsAt = in.readString();
    this.mDescription = in.readString();
    this.mTitle = in.readString();
    this.mImage = in.readString();
    this.mCreatedAt = in.readString();
    this.mAdmin = in.readParcelable(CompetitionAdmin.class.getClassLoader());
    this.mId = in.readString();
    this.communities = in.createTypedArrayList(CommunityModel.CREATOR);
    this.prizes = in.createStringArrayList();
    this.winners_announced = in.readByte() != 0;
  }

  public static final Parcelable.Creator<CompetitionModel> CREATOR = new Parcelable.Creator<CompetitionModel>() {
    @Override
    public CompetitionModel createFromParcel(Parcel source) {
      return new CompetitionModel(source);
    }

    @Override
    public CompetitionModel[] newArray(int size) {
      return new CompetitionModel[size];
    }
  };

  public List<JudgeModel> getmJudges() {
    return mJudges;
  }

  public int getmPostCount() {
    return mPostCount;
  }

  public void setmPostCount(int mPostCount) {
    this.mPostCount = mPostCount;
  }

  public int getmParticipantCount() {
    return mParticipantCount;
  }

  public void setmParticipantCount(int mParticipantCount) {
    this.mParticipantCount = mParticipantCount;
  }

  public void setmJudges(List<JudgeModel> mJudges) {
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

  public String getmParticipationHashtag() {
    return mParticipationHashtag;
  }

  public void setmParticipationHashtag(String mParticipationHashtag) {
    this.mParticipationHashtag = mParticipationHashtag;
  }

  public String getmCreatedAt() {
    return mCreatedAt;
  }

  public void setmCreatedAt(String mCreatedAt) {
    this.mCreatedAt = mCreatedAt;
  }

  public CompetitionAdmin getmAdmin() {
    return mAdmin;
  }

  public void setmAdmin(CompetitionAdmin mAdmin) {
    this.mAdmin = mAdmin;
  }

  public String getmId() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }

  public List<CommunityModel> getCommunities() {
    return communities;
  }

  public void setCommunities(List<CommunityModel> communities) {
    this.communities = communities;
  }

  public List<String> getPrizes() {
    return prizes;
  }

  public void setPrizes(List<String> prizes) {
    this.prizes = prizes;
  }

  public boolean isWinners_announced() {
    return winners_announced;
  }

  public void setWinners_announced(boolean winners_announced) {
    this.winners_announced = winners_announced;
  }

  public static Creator<CompetitionModel> getCREATOR() {
    return CREATOR;
  }

  @Override
  public String toString() {
    return "CompetitionModel{" +
      "mJudges=" + mJudges +
      ", mRules='" + mRules + '\'' +
      ", mEndsAt='" + mEndsAt + '\'' +
      ", mStartsAt='" + mStartsAt + '\'' +
      ", mDescription='" + mDescription + '\'' +
      ", mTitle='" + mTitle + '\'' +
      ", mImage='" + mImage + '\'' +
      ", mCreatedAt='" + mCreatedAt + '\'' +
      ", mAdmin=" + mAdmin +
      ", mId='" + mId + '\'' +
      ", communities=" + communities +
      ", prizes=" + prizes +
      ", winners_announced=" + winners_announced +
      '}';
  }
}
