package com.hapramp.main;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

/**
 * Created by Ankit on 5/16/2017.
 */

public class HapRampMain extends MultiDexApplication {

  private static Context context;

  public static Context getContext() {
    return context;
  }

  public void onCreate() {
    super.onCreate();
    HapRampMain.context = getApplicationContext();
    FirebaseApp.initializeApp(context);
  }

}
