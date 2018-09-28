package com.hapramp.analytics;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hapramp.notification.NotificationHandler;

public class HaprampFirebaseMessagingService extends FirebaseMessagingService{
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    if (remoteMessage.getData().size() > 0) {
      NotificationHandler.handleNotification(remoteMessage);
    }
  }
}
