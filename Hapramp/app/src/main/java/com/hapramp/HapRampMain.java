package com.hapramp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ankit on 5/16/2017.
 */

public class HapRampMain extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        HapRampMain.context = getApplicationContext();
    }

    public static Context getContext() {
        return HapRampMain.context;
    }
}
