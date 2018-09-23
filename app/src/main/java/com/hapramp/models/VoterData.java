package com.hapramp.models;

public class VoterData {
  private String username;
  private String perecent;
  private String reputation;

  public VoterData(String username, String perecent, String reputation) {
    this.username = username;
    this.perecent = perecent;
    this.reputation = reputation;
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

  public String getReputation() {
    return reputation;
  }

  public void setReputation(String reputation) {
    this.reputation = reputation;
  }
}
