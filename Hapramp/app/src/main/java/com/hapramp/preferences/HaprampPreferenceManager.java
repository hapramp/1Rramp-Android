package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.hapramp.api.DataServer;
import com.hapramp.main.HapRampMain;
import com.hapramp.logger.L;
import com.hapramp.models.UserAccountModel;
import com.hapramp.models.UserResponse;

/**
 * Created by Ankit on 5/15/2017.
 */

public class HaprampPreferenceManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static HaprampPreferenceManager mInstance;

    private static final String PREF_NAME = "hapramp_pref";
    private static final int PREF_MODE_PRIVATE = 1;

    public HaprampPreferenceManager(int i) {

        preferences = HapRampMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public static HaprampPreferenceManager getInstance() {
        if (mInstance == null) {
            mInstance = new HaprampPreferenceManager(0);
        }
        return mInstance;
    }

    public void clearPreferences(){
        FirebaseAuth.getInstance().signOut();
        DataServer.resetAPI();
        editor.clear();
        editor.apply();
        Log.d("Pref","Cleared Prefs.");
    }

    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("isLoggedIn",loggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn",false);
    }

    public void saveToken(String token){
        L.D.m("Pref","Saving user token "+token);
        editor.putString("userToken",token);
        editor.apply();
    }


    public String getUserToken(){
        return preferences.getString("userToken","--no--token--");
    }

    public void setUserInfoAvailable(boolean available){
        editor.putBoolean("userInfoAvailable",available);
        editor.apply();
    }


    public boolean isUserInfoAvailable(){
        return preferences.getBoolean("userInfoAvailable",false);
    }

    public void setUserId(String id){
        Log.d("Pref","setting user Id " +id);
        editor.putString("userId",id);
        editor.apply();
    }

    public String getUserId(){
        return preferences.getString("userId","");
    }

    public void setUserEmail(String s) {
        editor.putString("userEmail",s);
        editor.apply();
    }

    public void setUser(String user){
        L.D.m("Pref","User json "+user);
        editor.putString("userJson",user);
        editor.apply();
    }

    public UserResponse getUser(){
        return new Gson().fromJson(preferences.getString("userJson",""),UserResponse.class);
    }

    public void savePostDraft(String s) {
        editor.putString("post_draft",s);
        editor.apply();
    }

    public String getPostDraft(){
        return preferences.getString("post_draft","");
    }

    public void savePostMediaDraft(String filePath) {
        editor.putString("pmdraft",filePath);
        editor.apply();
    }

    public String getPostMediaDraft(){
        return preferences.getString("pmdraft","");
    }

    public void saveArticleDraft(String s){
        editor.putString("articleDraft",s);;
        editor.apply();
    }

    public String getArticleDraft() {
        return preferences.getString("articleDraft","");
    }
}
