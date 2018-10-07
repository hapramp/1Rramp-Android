package com.hapramp.analytics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;

public class EventReportUtils {
  public static final String EV = "events";
  public static final String AO = "app_opens";

  public static DatabaseReference getEventReportNodeRef(String app_version,
                                                        String date,
                                                        String username,
                                                        String timestamp) {
    String rootNode = HapRampMain.getFp();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    if (username == null) {
      username = "default_error_node";
    }
    return firebaseDatabase.getReference()
      .child(rootNode)
      .child(EV)
      .child(app_version)
      .child(date)
      .child(username)
      .child(timestamp);
  }

  public static DatabaseReference getAppOpenEventNodeRef(String app_version,
                                                         String date,
                                                         String timestamp) {
    String rootNode = HapRampMain.getFp();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (username.length() > 0) {
      return firebaseDatabase.getReference()
        .child(rootNode)
        .child(AO)
        .child(app_version)
        .child(date)
        .child(username)
        .child(timestamp);
    }
    return null;
  }

  public static DatabaseReference getDeviceIdNode(String username) {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String rootNode = HapRampMain.getFp();
    return firebaseDatabase.getReference()
      .child(rootNode)
      .child("devices")
      .child(username);
  }
}
