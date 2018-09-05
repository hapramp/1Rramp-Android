package com.hapramp.datastore.callbacks;

import java.util.ArrayList;

public interface FollowersCallback {
  void onFollowersAvailable(ArrayList<String> followers);

  void onFollowersFetchError(String err);
}
