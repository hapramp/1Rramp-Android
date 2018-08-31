package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.hapramp.main.HapRampMain;

public class DataStoreCachePreference {
  private static final String PREF_NAME = "1ramp_datastore_pref";
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor editor;
  private static DataStoreCachePreference mInstance;

  public DataStoreCachePreference() {
    preferences = HapRampMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = preferences.edit();
  }

  public static DataStoreCachePreference getInstance() {
    if (mInstance == null) {
      mInstance = new DataStoreCachePreference();
    }
    return mInstance;
  }

  public void saveResponse(String url, String response) {
    editor.putString("response_" + url, response);
    editor.apply();
  }

  public String getResponse(String url) {
    return preferences.getString("response_" + url, null);
  }
}
