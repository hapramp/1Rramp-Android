package com.hapramp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;


import com.hapramp.logger.L;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by IITGN on 08-06-2017.
 */


public class ForegroundCheck {

    public static boolean isAppIsInBackground() {

        Context context = HapRampMain.getContext();
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


}