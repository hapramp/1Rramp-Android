package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hapramp.api.DataServer;
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

    public void clearCachePreferences() {
        editor.clear();
        editor.apply();
    }

    public void setPostSynced(String segment){
        editor.putBoolean("segment_"+segment,true);
        editor.apply();
    }

    public Boolean isPostSynced(String segmentId){
        return preferences.getBoolean("segment_"+segmentId,false);
    }

    public boolean wasAllFeedCached() {
        return preferences.getBoolean("feedCached",false);
    }

    public void setAllFeedCached(boolean cached){
        editor.putBoolean("feedCached" , cached);
        editor.apply();
    }

    public boolean wasCommunityFeedCached(String communityTag) {
        return preferences.getBoolean("community_"+communityTag,false);
    }

    public void setCommunityFeedCached(String communityTag , boolean isCached){
        editor.putBoolean("community_"+communityTag,isCached);
        editor.apply();
    }

}
