package com.hapramp.notification;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hapramp.main.HapRampMain;
import com.hapramp.notification.model.BaseNotificationModel;
import com.hapramp.preferences.HaprampPreferenceManager;

public class FirebaseNotificationStore {
  public static final String NODE_NOTIFICATIONS = "notifications";
  public static final String NODE_IS_READ = "read";

  public static void saveNotification(final BaseNotificationModel baseNotificationModel) {
    new Thread() {
      @Override
      public void run() {
        String rootNode = HapRampMain.getFp();
        Looper.prepare();
        String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        if (username.length() > 0) {
          FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
          firebaseDatabase
            .getReference()
            .child(rootNode)
            .child(NODE_NOTIFICATIONS)
            .child(username)
            .child(baseNotificationModel.getNotificationId())
            .setValue(baseNotificationModel);
          Looper.loop();
        }
      }
    }.start();
  }

  public static DatabaseReference getNotificationsListNode() {
    String rootNode = HapRampMain.getFp();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (username.length() > 0) {
      FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
      return firebaseDatabase
        .getReference()
        .child(rootNode)
        .child(NODE_NOTIFICATIONS)
        .child(username);
    }
    return null;
  }


  public static void markAsRead(String notifId) {
    String rootNode = HapRampMain.getFp();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (username.length() > 0) {
      final DatabaseReference notificationRef = FirebaseDatabase.getInstance()
        .getReference()
        .child(rootNode)
        .child(NODE_NOTIFICATIONS)
        .child(username)
        .child(notifId);

      notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            notificationRef.child(NODE_IS_READ).setValue(true);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
      });
    }
  }
}
