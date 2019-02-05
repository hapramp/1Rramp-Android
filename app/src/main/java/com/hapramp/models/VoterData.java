package com.hapramp.models;

public class VoterData {
  private String username;
  private String perecent;
  private String voteValue;

  public VoterData(String username, String perecent, String voteValue) {
    this.username = username;
    this.perecent = perecent;
    this.voteValue = voteValue;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPerecent() {
    return perecent;
  }

  public void setPerecent(String perecent) {
    this.perecent = perecent;
  }

  public String getVoteValue() {
    return voteValue;
  }

  public void setVoteValue(String reputation) {
    this.voteValue = reputation;
  }
}
