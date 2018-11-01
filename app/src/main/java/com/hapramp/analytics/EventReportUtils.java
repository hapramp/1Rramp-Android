package com.hapramp.analytics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;

public class EventReportUtils {
  public static final String AO = "app_opens";

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
        .child(getFormattedUserName(username))
        .child(timestamp);
    }
    return null;
  }

  //Firebase Database paths must not contain '.', '#', '$', '[', or ']'
  public static String getFormattedUserName(String username) {
    return username
      .replace(".", "_dot_")
      .replace("#", "_hash_")
      .replace("$", "_dollar_")
      .replace("[", "_lb_")
      .replace("]", "_rb_");
  }

  public static DatabaseReference getDeviceIdNode(String username) {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String rootNode = HapRampMain.getFp();
    return firebaseDatabase.getReference()
      .child(rootNode)
      .child("devices")
      .child(getFormattedUserName(username));
  }
}
