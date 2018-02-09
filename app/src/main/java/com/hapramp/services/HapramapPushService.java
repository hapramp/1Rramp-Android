package com.hapramp.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hapramp.push.NotificationHandler;

/**
 * Created by Ankit on 12/27/2017.
 */

public class HapramapPushService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        NotificationHandler.handleNotification(remoteMessage,this);
        // Check if message contains a data payload.

    }

}
