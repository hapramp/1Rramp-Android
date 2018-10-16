package com.hapramp.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

public class EventReporter {
  public static void reportOpenEvent() {
    String date = MomentsUtils.getDate();
    String time = MomentsUtils.getCurrentTime();
    String ver = getAppVersion(HapRampMain.getContext());
    try {
      EventReportUtils.getAppOpenEventNodeRef(ver, date, time).setValue("open");
    }
    catch (Exception e) {

    }
  }

  public static String getAppVersion(Context context) {
    try {
      PackageInfo pInfo = context.getPackageManager().getPackageInfo("com.hapramp", 0);
      String versionCode = String.valueOf(pInfo.versionCode);
      return String.format("versionCode_%s", versionCode);
    }
    catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return "null";
  }

  public static void reportEventSession(Context context) {
  }

  public static void setOpenTime() {
  }

  public static void addEvent(String event) {
  }

  public static void reportDeviceId() {
    String token = FirebaseInstanceId.getInstance().getToken();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (username.length() > 0) {
      EventReportUtils
        .getDeviceIdNode(username).setValue(token);
    }
  }
}
