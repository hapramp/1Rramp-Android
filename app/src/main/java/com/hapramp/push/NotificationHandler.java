package com.hapramp.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.hapramp.main.HapRampMain;
import com.hapramp.datamodels.response.NotificationResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.services.ForegroundCheck;
import com.hapramp.utils.Constants;

import java.util.Map;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationHandler {

    private static NotificationPayloadModel notificationPayloadModel;

    public static void handleNotification(RemoteMessage remoteMessage, Context context) {
        //this is called when app is backgrounded

       notificationPayloadModel = new Gson().fromJson(remoteMessage.getData().get("data"),NotificationPayloadModel.class);

        if (new ForegroundCheck().isForeground(context)) {
            Log.d("Firebase", "app is in foreground");
            HaprampPreferenceManager.getInstance().incrementUnreadNotifications();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sendBroadcast();
                }
            });
        } else {
            Log.d("Firebase", "app is in background");
            NotificationUtils.showNotification(context, notificationPayloadModel);
        }
    }

    private static void sendBroadcast() {

        Intent intent = new Intent(Constants.ACTION_NOTIFICATION_UPDATE);
        HapRampMain.getContext().sendBroadcast(intent);

    }


}
