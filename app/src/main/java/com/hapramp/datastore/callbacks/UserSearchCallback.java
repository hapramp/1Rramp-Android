package com.hapramp.datastore.callbacks;

import java.util.List;

public interface UserSearchCallback {
  void onSearchingUsernames();
  void onUserSuggestionsAvailable(List<String> users);
  void onUserSuggestionsError(String msg);
}
