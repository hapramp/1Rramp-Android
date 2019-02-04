package com.hapramp.models;

import java.util.ArrayList;

public class CompetitionListResponse {
  private String lastId;
  private ArrayList<CompetitionModel> competitionModels;

  public String getLastId() {
    return lastId;
  }

  public void setLastId(String lastId) {
    this.lastId = lastId;
  }

  public ArrayList<CompetitionModel> getCompetitionModels() {
    return competitionModels;
  }

  public void setCompetitionModels(ArrayList<CompetitionModel> competitionModels) {
    this.competitionModels = competitionModels;
  }
}
