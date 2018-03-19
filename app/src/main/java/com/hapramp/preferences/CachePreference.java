package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.hapramp.main.HapRampMain;

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

    public void setPostSynced(String segment){
        editor.putBoolean("segment_"+segment,true);
        editor.apply();
    }

    public Boolean isPostSynced(String segmentId){
        return preferences.getBoolean("segment_"+segmentId,false);
    }


    public boolean wasFeedCached(String tag) {
        return preferences.getBoolean("feedCached_"+tag,false);
    }

    public void setFeedCached(String tag , boolean fetched){
        editor.putBoolean("feedCached_"+tag , fetched);
        editor.apply();
    }

}
