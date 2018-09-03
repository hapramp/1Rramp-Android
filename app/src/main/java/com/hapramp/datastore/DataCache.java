package com.hapramp.datastore;

import com.hapramp.preferences.DataStoreCachePreference;

public class DataCache {
  public static String get(String url) {
    return DataStoreCachePreference.getInstance().getResponse(url);
  }

  public static void cache(String url, String response) {
    DataStoreCachePreference.getInstance().saveResponse(url, response);
  }
}
