package com.hapramp.draft;

import com.google.gson.annotations.SerializedName;
import com.hapramp.models.JudgeModel;

import java.util.ArrayList;
import java.util.List;

public class ContestDraftModel {
  @SerializedName("draftId")
  private long DraftId;
  @SerializedName("competitionTitle")
  private String competitionTitle = "";
  @SerializedName("competitionDescription")
  private String competitionDescription = "";
  @SerializedName("competitionRules")
  private String competitionRules = "";
  @SerializedName("mCommunitySelection")
  private List<String> mCommunitySelection = new ArrayList<>();
  @SerializedName("customHashTags")
  private List<String> customHashTags = new ArrayList<>();
  @SerializedName("judges")
  private List<JudgeModel> judges = new ArrayList<>();
  @SerializedName("startTime")
  private String startTime = "";
  @SerializedName("startDate")
  private String startDate = "";
  @SerializedName("endTime")
  private String endTime = "";
  @SerializedName("endDate")
  private String endDate = "";
  @SerializedName("firstPrize")
  private String firstPrize = "";
  @SerializedName("competitionPosterDownloadUrl")
  private String competitionPosterDownloadUrl;

  public long getDraftId() {
    return DraftId;
  }

  public void setDraftId(long draftId) {
    DraftId = draftId;
  }

  public String getCompetitionTitle() {
    return competitionTitle;
  }

  public void setCompetitionTitle(String competitionTitle) {
    this.competitionTitle = competitionTitle;
  }

  public String getCompetitionDescription() {
    return competitionDescription;
  }

  public void setCompetitionDescription(String competitionDescription) {
    this.competitionDescription = competitionDescription;
  }

  public String getCompetitionRules() {
    return competitionRules;
  }

  public void setCompetitionRules(String competitionRules) {
    this.competitionRules = competitionRules;
  }

  public List<String> getmCommunitySelection() {
    return mCommunitySelection;
  }

  public void setmCommunitySelection(List<String> mCommunitySelection) {
    this.mCommunitySelection = mCommunitySelection;
  }

  public List<String> getCustomHashTags() {
    return customHashTags;
  }

  public void setCustomHashTags(List<String> customHashTags) {
    this.customHashTags = customHashTags;
  }

  public List<JudgeModel> getJudges() {
    return judges;
  }

  public void setJudges(ArrayList<JudgeModel> judges) {
    this.judges = judges;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getFirstPrize() {
    return firstPrize;
  }

  public void setFirstPrize(String firstPrize) {
    this.firstPrize = firstPrize;
  }

  public String getCompetitionPosterDownloadUrl() {
    return competitionPosterDownloadUrl;
  }

  public void setCompetitionPosterDownloadUrl(String competitionPosterDownloadUrl) {
    this.competitionPosterDownloadUrl = competitionPosterDownloadUrl;
  }

  @Override
  public String toString() {
    return "ContestDraftModel{" +
      "DraftId=" + DraftId +
      ", competitionTitle='" + competitionTitle + '\'' +
      ", competitionDescription='" + competitionDescription + '\'' +
      ", competitionRules='" + competitionRules + '\'' +
      ", mCommunitySelection=" + mCommunitySelection +
      ", customHashTags=" + customHashTags +
      ", judges=" + judges +
      ", startTime='" + startTime + '\'' +
      ", startDate='" + startDate + '\'' +
      ", endTime='" + endTime + '\'' +
      ", endDate='" + endDate + '\'' +
      ", firstPrize='" + firstPrize + '\'' +
      ", competitionPosterDownloadUrl='" + competitionPosterDownloadUrl + '\'' +
      '}';
  }
}
