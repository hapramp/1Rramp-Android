package com.hapramp.steem.models.user;

public class FollowItemModel {
  private boolean doIFollowHim;
  private String username;

  public FollowItemModel(boolean doIFollowHim, String username) {
    this.doIFollowHim = doIFollowHim;
    this.username = username;
  }

  public boolean isDoIFollowHim() {
    return doIFollowHim;
  }

  public void setDoIFollowHim(boolean doIFollowHim) {
    this.doIFollowHim = doIFollowHim;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
