package com.hapramp.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

public class EventReporter {
  static ArrayList<String> events = new ArrayList<>();
  static String open_time;
  static String close_time;

  public static void reportEvent(Context context) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < events.size(); i++) {
      stringBuilder.append(events.get(i))
        .append(" > ");
    }
    close_time = MomentsUtils.getCurrentTime();
    EventReportModel eventReportModel = new EventReportModel(open_time,
      close_time,
      stringBuilder.toString());
    EventReportUtils.getEventReportNodeRef(
      getAppVersion(context),
      MomentsUtils.getDate(),
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
      close_time
    ).setValue(eventReportModel).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        events.clear();
      }
    });
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

  public static void setOpenTime() {
    open_time = MomentsUtils.getCurrentTime();
  }

  public static void addEvent(String event) {
    events.add(event);
    Log.d("EventReport", events.toString());
  }

  public static void reportDeviceId() {
    String token = FirebaseInstanceId.getInstance().getToken();
    EventReportUtils
      .getDeviceIdNode(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()).setValue(token);
  }
}
