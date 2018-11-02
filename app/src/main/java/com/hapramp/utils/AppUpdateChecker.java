package com.hapramp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hapramp.main.HapRampMain;

public class AppUpdateChecker {
  public static void checkAppUpdatesNode(final Context context, final AppUpdateAvailableListener appUpdateAvailableListener) {
    String rootNode = "A6:AB:E7:62:D6:3C:6A:DC:F3:12:A0:24:B7:D3:06:CE:0D:FF:D3:3E";//HapRampMain.getFp();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    firebaseDatabase.getReference()
      .child(rootNode)
      .child("app_updates")
      .child("latest_version")
      .addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            String version = (String) dataSnapshot.getValue();
            try {
              int lv = Integer.valueOf(version);
              PackageInfo pInfo = context.getPackageManager().getPackageInfo("com.hapramp", 0);
              int myVersion = pInfo.versionCode;
              if (myVersion < lv) {
                if (appUpdateAvailableListener != null) {
                  appUpdateAvailableListener.onAppUpdateAvailable();
                }
              }
            }
            catch (Exception e) {
              Log.d("AppUpdater", e.toString());
            }
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
          Log.d("AppUpdater", databaseError.getMessage());
        }
      });
    ;
  }

  public interface AppUpdateAvailableListener {
    void onAppUpdateAvailable();
  }
}
