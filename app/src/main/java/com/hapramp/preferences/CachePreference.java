package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hapramp.api.DataServer;
import com.hapramp.main.HapRampMain;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.FeedResponse;

/**
 * Created by Ankit on 1/21/2018.
 */

public class CachePreference {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static CachePreference mInstance;

    private static final String PREF_NAME = "hapramp_cache_pref";
    private static final int PREF_MODE_PRIVATE = 1;

    public CachePreference() {

        preferences = HapRampMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();

    }

    public static CachePreference getInstance() {
        if (mInstance == null) {
            mInstance = new CachePreference();
        }
        return mInstance;
    }

    public void clearCachePreferences() {
        editor.clear();
        editor.apply();
    }

    public boolean isFeedResponseCached() {
        return preferences.getBoolean("feedCached", false);
    }

    public void cacheFeedResponse(FeedResponse feedResponse) {

        if (feedResponse != null) {
            editor.putString("feed_cache", new Gson().toJson(feedResponse));
            editor.putBoolean("feedCached", true);
            editor.apply();
        }

    }

    public FeedResponse getCachedFeedResponse() {
        return new Gson().fromJson(preferences.getString("feed_cache", ""), FeedResponse.class);
    }

    public void saveTrendingCacheAsJson(String json){
        editor.putString("trending_cache_feeds",json);
        editor.apply();
    }

    public FeedResponse getTrendingFeedResponseFromCache(){
        String json = preferences.getString("trending_cache_feeds",null);
        if (json != null) {
            return new Gson().fromJson(json,FeedResponse.class);
        }
        return null;
    }

    public void setTrendingCached(boolean cached) {
        editor.putBoolean("trending_cached", cached);
        editor.apply();
    }

    public boolean isTrendingCached() {
        return preferences.getBoolean("trending_cached", false);
    }

    public void saveHotCacheAsJson(String json){
        editor.putString("hot_cache_feeds",json);
        editor.apply();
    }

    public FeedResponse getHotFeedResponseFromCache(){
        String json = preferences.getString("hot_cache_feeds",null);
        if (json != null) {
            return new Gson().fromJson(json,FeedResponse.class);
        }
        return null;
    }

    public void setHotCached(boolean hotCached) {
        editor.putBoolean("hot_cached", hotCached);
        editor.apply();
    }

    public boolean isHotCached() {
        return preferences.getBoolean("hot_cached", false);
    }

    public void saveLatestCacheAsJson(String json){
        editor.putString("latest_cache_feeds",json);
        editor.apply();
    }

    public FeedResponse getLatestFeedResponseFromCache(){
        String json = preferences.getString("latest_cache_feeds",null);
        if (json != null) {
            return new Gson().fromJson(json,FeedResponse.class);
        }
        return null;
    }

    public void setLatestCached(boolean latestCached) {
        editor.putBoolean("latest_cached", latestCached);
        editor.apply();
    }

    public boolean isLatestCached() {
        return preferences.getBoolean("latest_cached", false);
    }



}
