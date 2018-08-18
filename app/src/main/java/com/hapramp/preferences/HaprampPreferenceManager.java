package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.hapramp.main.HapRampMain;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ankit on 5/15/2017.
 */

public class HaprampPreferenceManager {
  private static final String PREF_NAME = "1ramp_pref";
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor editor;
  private static HaprampPreferenceManager mInstance;

  public HaprampPreferenceManager() {
    preferences = HapRampMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = preferences.edit();
  }

  public static HaprampPreferenceManager getInstance() {
    if (mInstance == null) {
      mInstance = new HaprampPreferenceManager();
    }
    return mInstance;
  }

  public void clearPreferences() {
    editor.clear();
    editor.apply();
    setOnBoardingVisited();
  }

  public void setOnBoardingVisited() {
    editor.putBoolean("onBoardingDone", true);
    editor.apply();
  }

  public boolean isOnBoardingDone() {
    return preferences.getBoolean("onBoardingDone", false);
  }

  public boolean isLoggedIn() {
    return preferences.getBoolean("isLoggedIn", false);
  }

  public void setLoggedIn(boolean loggedIn) {
    editor.putBoolean("isLoggedIn", loggedIn);
    editor.apply();
  }

  public void saveUserToken(String accessToken) {
    editor.putString("accessToken", accessToken);
    editor.apply();
  }

  public void setTokenExpireTime(long time) {
    editor.putLong("tokenExpiryTime", time);
    editor.apply();
  }

  public long getTokenExpiryTime() {
    return preferences.getLong("tokenExpiryTime", -1);
  }

  public String getUserToken() {
    return preferences.getString("accessToken", "");
  }

  public String getSC2AccessToken() {
    return preferences.getString("sc2act", "");
  }

  public void setSC2AccessToken(String token) {
    editor.putString("sc2act", token);
    editor.apply();
  }

  public String getCurrentSteemUsername() {
    return preferences.getString("username", "");
  }

  public void saveCurrentSteemUsername(String username) {
    editor.putString("username", username);
    editor.apply();
  }

  public void saveCurrentUserInfoAsJson(String json) {
    editor.putString("c_user", json);
    editor.apply();
  }

  public String getCurrentUserInfoAsJson() {
    return preferences.getString("c_user", "");
  }

  public void saveAllCommunityListAsJson(String json) {
    editor.putString("allCommunity", json);
    editor.apply();
  }

  public String getAllCommunityAsJson() {
    return preferences.getString("allCommunity", "{}");
  }

  public void setCommunityTagToNamePair(String tag, String name) {
    editor.putString(tag + "_name", name);
    editor.apply();
  }

  public String getCommunityNameFromTag(String tag) {
    return preferences.getString(tag + "_name", "");
  }

  public void setCommunityTagToColorPair(String tag, String color) {
    editor.putString(tag + "_color", color);
    editor.apply();
  }

  public String getCommunityColorFromTag(String tag) {
    return preferences.getString(tag + "_color", "#009988");
  }

  public void saveUserSelectedCommunitiesAsJson(String json) {
    editor.putString("userSelectedCommunity", json);
    editor.apply();
  }

  public String getUserSelectedCommunityAsJson() {
    return preferences.getString("userSelectedCommunity", "");
  }

  public void saveUserProfile(String username, String json) {
    editor.putString("user_profile_data_" + username, json);
    editor.apply();
  }

  public String getUserProfile(String username) {
    return preferences.getString("user_profile_data_" + username, "");
  }


  public String getUserId() {
    return preferences.getString("userId", "");
  }

  public void incrementUnreadNotifications() {
    setUnreadNotification(getUnreadNotifications() + 1);
  }

  public void setUnreadNotification(int unreadNotification) {
    editor.putInt("unread_notif", unreadNotification);
    editor.apply();
  }

  public int getUnreadNotifications() {
    return preferences.getInt("unread_notif", 0);
  }

  public void saveCurrentUserFollowings(ArrayList<String> followings) {
    Set<String> set = new HashSet<String>();
    set.addAll(followings);
    editor.putStringSet("followings", set);
    editor.apply();
  }

  public Set<String> getFollowingsSet() {
    return preferences.getStringSet("followings", null);
  }

  public long nextPostCreationAllowedAt() {
    return preferences.getLong("next_post_creation_allowed_at", 0);
  }

  public void setLastPostCreatedAt(String time) {
    long nextPostCreationStartFrom = MomentsUtils.getMillisFromTime(time) + 300000;
    editor.putLong("next_post_creation_allowed_at", nextPostCreationStartFrom);
    editor.apply();
  }

}
