package com.hapramp.utils;

import android.content.Context;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.FollowingsCallback;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.ArrayList;

public class CompleteFollowingHelper implements FollowingsCallback {
  private final DataStore dataStore;
  ArrayList<String> followingList;
  private String lastUserInResult;
  private String myUsername;
  private FollowingsSyncCompleteListener followingsSyncCompleteListener;

  public CompleteFollowingHelper(Context context) {
    dataStore = new DataStore();
    followingList = new ArrayList<>();
    myUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  public void syncCompleteFollowingList() {
    fetchFollowingsFrom(null);
  }

  private void fetchFollowingsFrom(String startUser) {
    dataStore.requestFollowings(myUsername,startUser,this);
  }

  public void setFollowingsSyncCompleteListener(FollowingsSyncCompleteListener followingsSyncCompleteListener) {
    this.followingsSyncCompleteListener = followingsSyncCompleteListener;
  }

  @Override
  public void onFollowingsAvailable(ArrayList<String> followings) {
    if (lastUserInResult == null) {
      followingList.addAll(followings);
    } else {
      followings.remove(0);
      followingList.addAll(followings);
    }
    if (followings.size() > 0) {
      lastUserInResult = followings.get(followings.size() - 1);
    }

    //call ahead to fetch more
    if (followings.size() >= 19) {
      fetchFollowingsFrom(lastUserInResult);
    } else {
      HaprampPreferenceManager.getInstance().saveCurrentUserFollowings(followingList);
      if (followingsSyncCompleteListener != null) {
        followingsSyncCompleteListener.onSyncCompleted();
      }
    }
  }

  @Override
  public void onFollowingsFetchError(String err) {

  }

  public interface FollowingsSyncCompleteListener {
    void onSyncCompleted();
  }
}
