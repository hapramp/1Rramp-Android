package com.hapramp.datastore.callbacks;

public interface FollowInfoCallback {
  void onFollowInfoAvailable(int followers,int followings);
  void onFollowInfoError(String msg);
}
