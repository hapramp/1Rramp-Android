package com.hapramp.main;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * Created by Ankit on 5/16/2017.
 */

public class HapRampMain extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        HapRampMain.context = getApplicationContext();
        FirebaseApp.initializeApp(context);
    }

    public static Context getContext() {
        return HapRampMain.context;
    }
}
