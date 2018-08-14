package com.hapramp.utils;

import com.hapramp.preferences.HaprampPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import hapramp.walletinfo.NetworkUtils;

public class RepeatPostCreationUtils {
  public static void syncLastPostCreationTime() {
    new Thread() {
      @Override
      public void run() {
        NetworkUtils networkUtils = new NetworkUtils();
        networkUtils.setNetworkResponseCallback(new NetworkUtils.NetworkResponseCallback() {
          @Override
          public void onResponse(String response) {
            try {
              JSONObject jsonObject = new JSONObject(response);
              String lastPost = jsonObject
                .getJSONObject("user")
                .getString("last_root_post");
              HaprampPreferenceManager.getInstance().setLastPostCreatedAt(lastPost);
            }
            catch (JSONException e) {
              e.printStackTrace();
            }

          }

          @Override
          public void onError(String e) {

          }
        });
        networkUtils.request("https://steemit.com/@" +
          HaprampPreferenceManager.getInstance().getCurrentSteemUsername() +
          ".json", "GET", "");
      }
    }.start();
  }
}
