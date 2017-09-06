package com.hapramp.chat.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.hapramp.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;

/**
 * Created by Ankit on 9/5/2017.
 */

public class UserAccountPreference {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static UserAccountPreference mInstance;

    private static final String PREF_NAME = "chats_pref";
    private static final int PREF_MODE_PRIVATE = 1;

    public UserAccountPreference() {

        preferences = HapRampMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public static UserAccountPreference getInstance() {
        if (mInstance == null) {
            mInstance = new UserAccountPreference();
        }
        return mInstance;
    }

    public void clearPreferences(){
        editor.clear();
        editor.commit();
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

    public void setUserEmail(String s) {
        editor.putString("userEmail",s);
        editor.commit();
    }

    public String getUserEmail(){
        return preferences.getString("userEmail","");
    }

}
