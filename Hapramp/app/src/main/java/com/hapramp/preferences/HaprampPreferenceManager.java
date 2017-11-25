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

    private void setLandingPagesVisited(boolean visited){
        editor.putBoolean("isLandingPagesViewed",visited);
        editor.apply();
    }

    public boolean isLandingPagesVisited() {
        return preferences.getBoolean("isLandingPagesViewed",true);
    }

    public void saveToken(String token){
        L.D.m("Pref","Saving user token "+token);
        editor.putString("userToken",token);
        editor.apply();
    }

    public String getUserToken(){
        return preferences.getString("userToken","Token token");
    }

    public void saveUserFirstName(String firstname) {
        editor.putString("ufname",firstname);
        editor.apply();
    }

    public void saveUserLastName(String lastname) {
        editor.putString("ulname",lastname);
        editor.apply();
    }

    public String getFname(){
        return preferences.getString("ufname","");
    }

    public String getLname(){
        return preferences.getString("ulname","");
    }


    public void setUserFirstName(String firstname) {
        editor.putString("fname",firstname);
        editor.apply();
    }

    public String getFirstName(){
        return preferences.getString("fname","");
    }

    public void setUserLastName(String lastname){
        editor.putString("lname",lastname);
        editor.apply();
    }

    public String getLastName(){
        return preferences.getString("lname","");
    }

    public void setUserId(String id){
        Log.d("Pref","setting user Id " +id);
        editor.putString("userId",id);
        editor.apply();
    }

    public String getUserId(){
        return preferences.getString("userId","");
    }

    public void setUserProfilePicUrl(String profilePic) {
        editor.putString("profilePic",profilePic);
        editor.apply();
    }

    public String getProfilePicUrl(){
        return preferences.getString("profilePic","");
    }

    public void setNotificationsCount(int size) {

        editor.putString("noti_c",size+"");
        editor.apply();
    }

    public String getNotificationCount(){

        return preferences.getString("noti_c","0");
    }

    public void incrementNotificationCount() {
        setNotificationsCount(Integer.parseInt(getNotificationCount())+1);
    }

    public void setDeviceId(String refreshedToken) {
        editor.putString("devId",refreshedToken);
        editor.apply();
    }

    public String getDeviceId(){
        return preferences.getString("devId","");
    }

    public void setUserEmail(String s) {
        editor.putString("userEmail",s);
        editor.apply();
    }

    public String getUserEmail(){
        return preferences.getString("userEmail","");
    }

    public void setUser(String user){
        L.D.m("Pref","User json "+user);
        editor.putString("userJson",user);
        editor.apply();
    }

    public UserResponse getUser(){
        return new Gson().fromJson(preferences.getString("userJson",""),UserResponse.class);
    }

}
