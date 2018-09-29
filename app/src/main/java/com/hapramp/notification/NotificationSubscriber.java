package com.hapramp.notification;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hapramp.preferences.HaprampPreferenceManager;

public class NotificationSubscriber {
  public static void subscribeForUserTopic() {
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    FirebaseMessaging.getInstance().subscribeToTopic(username).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Log.d("NotificationSubs","done");
        HaprampPreferenceManager.getInstance().setUserTopicSubscribed(true);
      }
    });
  }

  public static void unsubscribeForUserTopic() {
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    FirebaseMessaging.getInstance().unsubscribeFromTopic(username).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Log.d("NotificationSubs","un done");
        HaprampPreferenceManager.getInstance().setUserTopicSubscribed(false);
      }
    });
  }
}
