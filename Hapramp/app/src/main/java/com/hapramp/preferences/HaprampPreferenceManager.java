package com.hapramp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.hapramp.HapRampMain;

/**
 * Created by Ankit on 5/15/2017.
 */

public class HaprampPreferenceManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static HaprampPreferenceManager mInstance;

    private static final String PREF_NAME = "hapramp_pref";
    private static final int PREF_MODE_PRIVATE = 1;

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

    public void clearPreferences(){
        editor.clear();
        editor.commit();
    }

    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("isLoggedIn",loggedIn);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn",false);
    }

    private void setLandingPagesVisited(boolean visited){
        editor.putBoolean("isLandingPagesViewed",visited);
        editor.commit();
    }

    public boolean isLandingPagesVisited() {
        return preferences.getBoolean("isLandingPagesViewed",true);
    }

    public void saveToken(String token){
        editor.putString("userToken",token);
        editor.commit();
    }

    public String getUserToken(){
        return preferences.getString("userToken","--token");
    }

    public void saveUserFirstName(String firstname) {
        editor.putString("ufname",firstname);
        editor.commit();
    }

    public void saveUserLastName(String lastname) {
        editor.putString("ulname",lastname);
        editor.commit();
    }

    public String getFname(){
        return preferences.getString("ufname","");
    }

    public String getLname(){
        return preferences.getString("ulname","");
    }


    public void setUserFirstName(String firstname) {
        editor.putString("fname",firstname);
        editor.commit();
    }

    public String getFirstName(){
        return preferences.getString("fname","");
    }

    public void setUserLastName(String lastname){
        editor.putString("lname",lastname);
        editor.commit();
    }

    public String getLastName(){
        return preferences.getString("lname","");
    }

    public void setUserId(String id){
        editor.putString("userId",id);
        editor.commit();
    }

    public String getUserId(){
        return preferences.getString("userId","");
    }

    public void setUserProfilePicUrl(String profilePic) {
        editor.putString("profilePic",profilePic);
        editor.commit();
    }

    public String getProfilePicUrl(){
        return preferences.getString("profilePic","");
    }

    public void setNotificationsCount(int size) {

        editor.putString("noti_c",size+"");
        editor.commit();
    }

    public String getNotificationCount(){

        return preferences.getString("noti_c","0");
    }

    public void incrementNotificationCount() {
        setNotificationsCount(Integer.parseInt(getNotificationCount())+1);
    }

    public void setDeviceId(String refreshedToken) {
        editor.putString("devId",refreshedToken);
        editor.commit();
    }

    public String getDeviceId(){
        return preferences.getString("devId","");
    }

    public void setUserEmail(String s) {
        editor.putString("userEmail",s);
        editor.commit();
    }

    public String getUserEmail(){
        return preferences.getString("userEmail","");
    }

}
