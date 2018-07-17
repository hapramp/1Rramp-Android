package com.hapramp.interfaces;

/**
 * Created by Ankit on 11/16/2017.
 */

public interface FollowUserCallback {
  void onUserFollowSet(boolean state);

  void onUserFollowSetFailed(boolean state);
}
