package com.hapramp.models;

public class VoterData {
  private String username;
  private String perecent;
  private long rshare;
  private String voteValue;

  public VoterData(String username, String perecent,long rshare, String voteValue) {
    this.username = username;
    this.perecent = perecent;
    this.rshare = rshare;
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

  public long getRshare() {
    return rshare;
  }

  public void setRshare(long rshare) {
    this.rshare = rshare;
  }

  public String getVoteValue() {
    return voteValue;
  }

  public void setVoteValue(String reputation) {
    this.voteValue = reputation;
  }

  @Override
  public String toString() {
    return "VoterData{" +
      "username='" + username + '\'' +
      ", perecent='" + perecent + '\'' +
      ", rshare=" + rshare +
      ", voteValue='" + voteValue + '\'' +
      '}';
  }
}
