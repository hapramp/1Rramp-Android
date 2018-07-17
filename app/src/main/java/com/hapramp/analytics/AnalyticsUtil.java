package com.hapramp.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hapramp.main.HapRampMain;

/**
 * Created by Ankit on 5/14/2018.
 */

public class AnalyticsUtil {
  private static FirebaseAnalytics firebaseAnalytics;

  public static void logEvent(String event) {
    Bundle bundle = new Bundle();
    bundle.putString("hapramp_event", event);
    getInstance(HapRampMain.getContext()).logEvent("Event:" + event, bundle);
  }

  public static FirebaseAnalytics getInstance(Context context) {
    if (firebaseAnalytics == null) {
      firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }
    return firebaseAnalytics;
  }

}
