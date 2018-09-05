package com.hapramp.datastore.callbacks;

import java.util.ArrayList;

public interface FollowingsCallback {
  void onFollowingsAvailable(ArrayList<String> followings);

  void onFollowingsFetchError(String err);
}
