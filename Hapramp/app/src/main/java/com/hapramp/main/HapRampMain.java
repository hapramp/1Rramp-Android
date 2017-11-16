package com.hapramp.main;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

/**
 * Created by Ankit on 5/16/2017.
 */

public class HapRampMain extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        HapRampMain.context = getApplicationContext();
        Fresco.initialize(context);
        Stetho.initializeWithDefaults(context);
    }

    public static Context getContext() {
        return HapRampMain.context;
    }
}
