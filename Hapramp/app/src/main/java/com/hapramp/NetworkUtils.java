package com.hapramp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Ankit on 5/16/2017.
 */
public class NetworkUtils {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus() {

        Context context = HapRampMain.getContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString() {

        int conn = NetworkUtils.getConnectivityStatus();
        int status = -1;
        if (conn == NetworkUtils.TYPE_WIFI) {
            status = 1;
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status = 2;
        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
            status = 0;
        }
        return status;
    }
}
