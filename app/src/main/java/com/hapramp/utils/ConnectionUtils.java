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
    return mobileData.isConnected() || wifi.isConnectedOrConnecting();
  }
}

