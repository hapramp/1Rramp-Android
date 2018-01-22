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

    public void setLastPostSyncTime(String time){
        editor.putString("lastPostFetchTime",time);
        editor.apply();
    }

    public String getLastPostSyncTime(){
        return preferences.getString("lastPostFetchTime",null);
    }


}
