package com.hapramp.analytics;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class HaprampInstanceIdService extends FirebaseInstanceIdService {
  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    EventReporter.reportDeviceId();
  }
}
