package com.hapramp.models;

import com.hapramp.utils.SteemPowerCalc;

public class DelegationModel {
  private String delegator;
  private String delegatee;
  private String vests;
  private String time;

  public DelegationModel(String delegator, String delegatee, String vests, String time) {
    this.delegator = delegator;
    this.delegatee = delegatee;
    this.vests = vests;
    this.time = time;
  }

  public String getDelegator() {
    return delegator;
  }

  public void setDelegator(String delegator) {
    this.delegator = delegator;
  }

  public String getDelegatee() {
    return delegatee;
  }

  public void setDelegatee(String delegatee) {
    this.delegatee = delegatee;
  }

  public String getDelegatedVests() {
    return vests;
  }

  public double getDelegatedSteemPower() {
    return SteemPowerCalc.calculateSteemPower(Double.valueOf(vests.split(" ")[0]));
  }

  public void setVests(String vests) {
    this.vests = vests;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
