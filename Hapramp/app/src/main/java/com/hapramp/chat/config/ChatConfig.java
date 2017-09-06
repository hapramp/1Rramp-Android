package com.hapramp.chat.config;

import android.os.SystemClock;

import com.hapramp.preferences.HaprampPreferenceManager;

/**
 * Created by Ankit on 7/20/2017.
 */

public class ChatConfig {

    private String myID;

    private static ChatConfig mInstance;

    public static ChatConfig getInstance() {
        if (mInstance == null) {
            mInstance = new ChatConfig();
        }
        return mInstance;
    }

    public ChatConfig() {
        this.myID = HaprampPreferenceManager.getInstance().getUserId();
    }

    public String getChatRoomId(String companionID) {
        return new StringBuilder().append("cr_" + myID + "_" + companionID).toString();
    }

    public String getMessageID(String companionID) {
        return new StringBuilder().append("msg_" + myID + "_" + companionID + "_" + HaprampTime.getInstance().getTime()).toString();
    }

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
        HaprampPreferenceManager.getInstance().setUserId(myID);
    }


}
