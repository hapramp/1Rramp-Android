package com.hapramp.services;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;


import com.hapramp.main.HapRampMain;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by IITGN on 08-06-2017.
 */



public class ForegroundCheck extends AsyncTask<Context,Void,Boolean> {

    @Override
    protected Boolean doInBackground(Context... contexts) {
        //return isAppIsInBackground(contexts[0].getApplicationContext());
        return isForeground(contexts[0].getApplicationContext());
    }

    public boolean isForeground(Context context){

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = mActivityManager.getRunningAppProcesses();
        String haprampPackage = "com.hapramp";

        for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcesses){
            if(appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                if(appProcessInfo.processName.equals(haprampPackage)){
                    Log.d("ForegroundCheck", "in foreground");
                    return true;
                }

            }
        }
        Log.d("ForegroundCheck", "in background");
        return false;

    }

    private boolean isAppIsInBackground(Context context) {

        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //Log.d("ForegroundCheck","process imp"+processInfo.importance+" process "+processInfo.processName);
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