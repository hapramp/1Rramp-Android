package com.hapramp.datastore;

import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.DataStoreCachePreference;

public class DataCache {
  public static String get(String url) {
    return DataStoreCachePreference.getInstance(HapRampMain.getContext()).getResponse(url);
  }

  public static void cache(String url, String response) {
    DataStoreCachePreference.getInstance(HapRampMain.getContext()).saveResponse(url, response);
  }
}
