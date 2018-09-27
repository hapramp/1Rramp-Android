package com.hapramp.analytics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hapramp.main.HapRampMain;

public class EventReportUtils {
  public static final String ROOT = "events";

  public static DatabaseReference getEventReportNodeRef(String app_version,
                                                        String date,
                                                        String username,
                                                        String timestamp) {
    String rootNode = HapRampMain.getFp();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    if (username == null) {
      username = FirebaseInstanceId.getInstance().getToken();
    }
    return firebaseDatabase.getReference()
      .child(rootNode)
      .child(ROOT)
      .child(app_version)
      .child(date)
      .child(username)
      .child(timestamp);
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
