package com.hapramp.utils;

import android.content.Context;
import android.util.Log;

import com.hapramp.api.FollowingApi;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.ArrayList;

public class CompleteFollowingHelper implements FollowingApi.FollowingCallback {
  private final FollowingApi followingApi;
  ArrayList<String> followingList;
  private String lastUserInResult;
  private String myUsername;
  private FollowingsSyncCompleteListener followingsSyncCompleteListener;

  public CompleteFollowingHelper(Context context) {
    followingApi = new FollowingApi(context);
    followingApi.setFollowingCallback(this);
    followingList = new ArrayList<>();
    myUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  public void syncCompleteFollowingList() {
    fetchFollowingsFrom(null);
  }

  private void fetchFollowingsFrom(String startUser) {
    Log.d("FollowingsList", "loading from:" + startUser);
    followingApi.requestFollowings(myUsername, startUser);
  }

  @Override
  public void onFollowings(ArrayList<String> followings) {
    Log.d("FollowingsList", "loaded :" + followings.size());
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
  public void onError() {

  }

  public void setFollowingsSyncCompleteListener(FollowingsSyncCompleteListener followingsSyncCompleteListener) {
    this.followingsSyncCompleteListener = followingsSyncCompleteListener;
  }

  public interface FollowingsSyncCompleteListener {
    void onSyncCompleted();
  }
}
