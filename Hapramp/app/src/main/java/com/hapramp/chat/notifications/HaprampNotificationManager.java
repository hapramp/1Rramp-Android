package com.hapramp.chat.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.hapramp.HapRampMain;
import com.hapramp.chat.models.Message;


/**
 * Created by IITGN on 08-06-2017.
 */

public class HaprampNotificationManager {

    private static Context context;
    private static HaprampNotificationManager mInstance;

    public HaprampNotificationManager() {
        this.context = HapRampMain.getContext();
    }

    public static HaprampNotificationManager getInstance() {
        if (mInstance == null) {
            mInstance = new HaprampNotificationManager();
        }
        return mInstance;
    }


    public void generateNotification(Message message) {

    }
}
