package com.hapramp.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

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

public class NotificationManager {

    private static Map<String, String> map;

    public static void handleNotification(RemoteMessage remoteMessage){
        //this is called when app is backgrounded

        map = remoteMessage.getData();
        NotificationResponse.Notification notificationObject = new NotificationResponse.Notification(
                Integer.valueOf(map.get("id")),
                map.get("content"),
                map.get("created_at"),
                Integer.valueOf(map.get("user_id")),
                Integer.valueOf(map.get("actor_id")),
                map.get("action"),
                map.get("arg1"),
                Boolean.valueOf(map.get("is_read")));

        if (!ForegroundCheck.isAppIsInBackground()) {
            // if foreground
            // update the notification in shared pref
            HaprampPreferenceManager.getInstance().incrementUnreadNotifications();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast();
                }
            });
        }else{
            // generate a notification and add it to system tray
            NotificationUtils.showNotification(HapRampMain.getContext(),notificationObject);
        }

    }

    private static void sendBroadcast() {

        Intent intent = new Intent(Constants.ACTION_NOTIFICATION_UPDATE);
        HapRampMain.getContext().sendBroadcast(intent);

    }


}
