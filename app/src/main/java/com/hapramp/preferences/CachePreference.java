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

}
