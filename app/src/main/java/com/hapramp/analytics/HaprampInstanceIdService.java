package com.hapramp.analytics;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class HaprampInstanceIdService extends FirebaseInstanceIdService {
  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    Log.d("FirebaseToken", FirebaseInstanceId.getInstance().getToken());
  }
}
