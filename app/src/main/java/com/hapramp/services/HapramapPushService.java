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
        Log.d("Firebase", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData());
        }
        NotificationHandler.handleNotification(remoteMessage,this);
    }
}
