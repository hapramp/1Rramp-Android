package com.hapramp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Ankit on 2/4/2018.
 */

public class ConnectionUtils {

  public static boolean isConnected(Context context) {

    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    final android.net.NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    final android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    if (mobileData.isConnected()) {
      return true;
    } else if (wifi.isConnected()) {
      return true;
    }

//        context.startActivity(new Intent(context, ErrorSplash.class));
//        ((AppCompatActivity) context).finish();

    return false;
  }

  // for wifi service [where login is required before internet ]
  private static boolean isWorking() {
    try {
      Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
      int returnVal = p1.waitFor();
      return (returnVal == 0);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

}

