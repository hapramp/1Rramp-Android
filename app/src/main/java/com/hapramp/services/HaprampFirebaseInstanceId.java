package com.hapramp.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hapramp.push.Notifyer;

/**
 * Created by Ankit on 12/27/2017.
 */

public class HaprampFirebaseInstanceId extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d("FirebaseService", "Refreshed token: " + refreshedToken);
    Notifyer.updateDeviceToken();
  }
}
