package com.hapramp.utils;

import com.hapramp.preferences.HaprampPreferenceManager;

public class AccessTokenValidator {
  public static long getNextExpiryTime() {
    long now = System.currentTimeMillis();
    return now + 604800000;
  }

  public static boolean isTokenExpired() {
    long now = System.currentTimeMillis();
    long expiryTime = HaprampPreferenceManager.getInstance().getTokenExpiryTime();
    return now > expiryTime;
  }
}
