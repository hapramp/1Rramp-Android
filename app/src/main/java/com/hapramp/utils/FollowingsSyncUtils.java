package com.hapramp.utils;

import android.content.Context;

public class FollowingsSyncUtils {
  public static void syncFollowings(Context context) {
    CompleteFollowingHelper completeFollowingHelper = new CompleteFollowingHelper(context);
    completeFollowingHelper.syncCompleteFollowingList();
  }

  public static void syncFollowings(
    Context context, CompleteFollowingHelper.FollowingsSyncCompleteListener followingsSyncCompleteListener) {
    CompleteFollowingHelper completeFollowingHelper = new CompleteFollowingHelper(context);
    completeFollowingHelper.setFollowingsSyncCompleteListener(followingsSyncCompleteListener);
    completeFollowingHelper.syncCompleteFollowingList();
  }
}
