package com.hapramp.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.hapramp.main.HapRampMain;
import com.hapramp.models.response.NotificationResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.services.ForegroundCheck;
import com.hapramp.utils.Constants;

import java.util.Map;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationHandler {

    private static Map<String, String> map;

    public static void handleNotification(RemoteMessage remoteMessage,Context context) {
        //this is called when app is backgrounded

        map = remoteMessage.getData();
        final NotificationResponse.Notification notificationObject = new NotificationResponse.Notification(
                Integer.valueOf(map.get("id")),
                map.get("content"),
                map.get("created_at"),
                Integer.valueOf(map.get("user_id")),
                Integer.valueOf(map.get("actor_id")),
                map.get("action"),
                map.get("arg1"),
                Boolean.valueOf(map.get("is_read")));

        if (new ForegroundCheck().isForeground(context)) {
            // if foreground
            // update the notification in shared pref
            Log.d("Firebase", "app is in foreground");
            HaprampPreferenceManager.getInstance().incrementUnreadNotifications();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast();
                }
            });
        } else {
            // generate a notification and add it to system tray

            Log.d("Firebase", "app is in background");

            NotificationUtils.showNotification(context, notificationObject);

        }

    }

    private static void sendBroadcast() {

        Intent intent = new Intent(Constants.ACTION_NOTIFICATION_UPDATE);
        HapRampMain.getContext().sendBroadcast(intent);

    }


}
