package com.hapramp.interfaces;

import java.util.ArrayList;

public interface RebloggedUserFetchCallback {
  void onRebloggedUserFetched(String reqTag, ArrayList<String> users);
  void onRebloggedUserFailed();
}
