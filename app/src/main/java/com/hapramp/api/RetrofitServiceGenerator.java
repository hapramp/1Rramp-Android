package com.hapramp.api;

import com.hapramp.preferences.HaprampPreferenceManager;

public class RetrofitServiceGenerator {

  public static HaprampAPI getService() {
    String token = HaprampPreferenceManager.getInstance().getUserToken();
    return HaprampApiClient.getClient(token).create(HaprampAPI.class);
  }
  public static HaprampAPI getV3Service() {
    String token = HaprampPreferenceManager.getInstance().getUserToken();
    return HaprampApiClient.getV3Client(token).create(HaprampAPI.class);
  }
}
