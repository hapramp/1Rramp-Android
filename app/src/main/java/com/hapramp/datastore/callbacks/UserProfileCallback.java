package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.User;


public interface UserProfileCallback {
  void onWeAreFetchingUserProfile();

  void onUserProfileAvailable(User user, boolean isFreshData);

  void onUserProfileFetchError(String err);
}