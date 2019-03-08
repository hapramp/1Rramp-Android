package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hapramp.main.HapRampMain;
import com.hapramp.models.AppServerUserModel;
import com.hapramp.models.MCListWrapper;
import com.hapramp.models.MicroCommunity;
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
    DataStoreCachePreference.getInstance(HapRampMain.getContext()).clearCaches();
    setOnBoardingVisited();
  }

  public void setOnBoardingVisited() {
    editor.putBoolean("onBoardingDone", true);
    editor.apply();
  }

  public boolean isRatingSliderEnabled() {
    return preferences.getBoolean("rate_slider", false);
  }

  public void setRatingSliderEnabled(boolean enabled) {
    editor.putBoolean("rate_slider", enabled);
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

  public void saveCurrentSteemUserInfoAsJson(String json) {
    editor.putString("c_user", json);
    editor.apply();
  }

  public String getCurrentSteemUserInfoAsJson() {
    return preferences.getString("c_user", "");
  }

  public void saveAllCommunityListAsJson(String json) {
    editor.putString("allCommunity", json);
    editor.apply();
  }

  public String getAllCommunityAsJson() {
    return preferences.getString("allCommunity", "");
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
    editor.commit();
    Log.d("UserInfo","saving followings "+set.toString());
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

  public int getDeviceWidth() {
    return preferences.getInt("deviceWidth", 0);
  }

  public void setDeviceWidth(int width) {
    editor.putInt("deviceWidth", width);
    editor.apply();
  }

  public int getImageDowngradeFactor() {
    return preferences.getInt("image_load_down_grade_factor", 2);
  }

  public void setImageLoaddownGradeFactor(int downGradeFactor) {
    editor.putInt("image_load_down_grade_factor", downGradeFactor);
    editor.apply();
  }

  public void saveVestsPerSteem(double vestsPerSteem) {
    editor.putFloat("vests_per_steem", (float) vestsPerSteem);
    editor.apply();
  }

  public float getVestsPerSteem() {
    return preferences.getFloat("vests_per_steem", 0);
  }

  public boolean isUserTopicSubscribed() {
    return preferences.getBoolean("user_topic_subs", false);
  }

  public void setUserTopicSubscribed(boolean subscribed) {
    editor.putBoolean("user_topic_subs", subscribed);
    editor.apply();
  }

  public void setShowPushNotifications(boolean subscribe) {
    editor.putBoolean("show_notifications", subscribe);
    editor.apply();
  }

  public boolean shouldShowPushNotifications() {
    return preferences.getBoolean("show_notifications", true);
  }

  public void setCompetitionCreateEligibility(boolean eligible) {
    editor.putBoolean("eligible_for_competition", eligible);
    editor.apply();
  }

  public boolean isEligibleForCompetitionCreation() {
    return preferences.getBoolean("eligible_for_competition", false);
  }

  public void saveGlobalPropsInfo(String globalePropsResponseJson) {
    editor.putString("globalprops", globalePropsResponseJson);
    editor.apply();
  }

  public String getGlobalProps() {
    return preferences.getString("globalprops", null);
  }

  public int getPercentSteemDollars() {
    //100% SBD by default
    return preferences.getInt("percentSteemDollars", 10000);
  }

  public void setPercentSteemDollars(int percent) {
    editor.putInt("percentSteemDollars", percent);
    editor.apply();
  }

  public String getMaxAcceptedPayout() {
    return preferences.getString("maxAcceptedPayout", "1000000.000 SBD");
  }

  public void setMaxAcceptedPayout(String maxAcceptedPayout) {
    editor.putString("maxAcceptedPayout", maxAcceptedPayout);
    editor.apply();
  }

  public boolean getAllowVotes() {
    return preferences.getBoolean("allowVotes", true);
  }

  public boolean getAllowCurationRewards() {
    return preferences.getBoolean("allowCurationRewards", true);
  }

  public long getLastDraftSyncTime() {
    return preferences.getLong("last_draft_sync", 0);
  }

  public void setLastDraftSyncTime() {
    editor.putLong("last_draft_sync", System.currentTimeMillis());
    editor.apply();
  }

  public void saveCurrentAppServerUserAsJson(String json) {
    editor.putString("c_app_server_user", json);
    editor.apply();
  }

  public AppServerUserModel getCurrentAppserverUser() {
    String json = preferences.getString("c_app_server_user", null);
    if (json != null) {
      return new Gson().fromJson(json, AppServerUserModel.class);
    }
    return null;
  }

  public void saveMicroCommunities(ArrayList<MicroCommunity> microCommunities) {
    MCListWrapper mcListWrapper = new MCListWrapper();
    mcListWrapper.setMicroCommunities(microCommunities);
    String json = new Gson().toJson(mcListWrapper);
    editor.putString("mcListJson", json);
    editor.apply();
  }

  public ArrayList<MicroCommunity> getMicroCommunities() {
    ArrayList<MicroCommunity> microCommunities = new ArrayList<>();
    String savedJson = preferences.getString("mcListJson", null);
    if (savedJson != null) {
      microCommunities = new Gson().fromJson(savedJson, MCListWrapper.class).getMicroCommunities();
    }
    return microCommunities;
  }

}
