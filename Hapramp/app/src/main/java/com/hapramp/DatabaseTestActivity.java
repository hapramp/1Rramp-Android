package com.hapramp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import bxute.activity.ChatListActivity;
import bxute.activity.ChatRoomActivity;
import bxute.config.ChatConfig;
import bxute.config.Constants;
import bxute.config.LocalTimeManager;
import bxute.config.MessageStatus;
import bxute.config.UserPreference;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.ChatRoom;
import bxute.models.Message;

public class DatabaseTestActivity extends AppCompatActivity {

    String t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        String rajatId = "Rajat";

//        injectChatRoom(rajatId);
//
//        FirebaseDatabaseManager.setOnlineStatus(rajatId,"Online");
//        FirebaseDatabaseManager.setOnlineStatus("Online");
//
//        FirebaseDatabaseManager.registerDevice(rajatId);
//        FirebaseDatabaseManager.registerDevice();
//
        test();

    }

    private void test(){
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
    }

    private void injectChatRoom(String chanchalId){

        String MyId = UserPreference.getUserId();

        Message message = new Message(
                ChatConfig.getMessageID(chanchalId,MyId),  // message from chanchal
                "Message from "+chanchalId,
                LocalTimeManager.getInstance().getDateTime(),
                "",
                "",
                MessageStatus.STATUS_SENT,
                ChatConfig.getChatRoomId(chanchalId,MyId),
                chanchalId,MyId
        );

        FirebaseDatabaseManager.addMessage(message);

        ChatRoom myChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(chanchalId,MyId),
                chanchalId,
                "Ankit Kumar",
                message,
                0,
                0,
                "https://lh3.googleusercontent.com/-oB-qfI6Mmwc/AAAAAAAAAAI/AAAAAAAAAAA/ACnBePYLfOpmginapw-BdNcUE_VORECrKA/s48-c-mo/photo.jpg",
                ChatConfig.STATUS_NOT_TYPING
        );

        // get self node and add message
        FirebaseDatabaseManager.createOrUpdateChatroom(myChatRoom);

        message.setChatRoomId(ChatConfig.getChatRoomId(MyId,chanchalId));
        ChatRoom compChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(MyId,chanchalId),
                MyId,
                "Cr- "+chanchalId,
                message,
                0,
                0,
                "https://lh3.googleusercontent.com/-oB-qfI6Mmwc/AAAAAAAAAAI/AAAAAAAAAAA/ACnBePYLfOpmginapw-BdNcUE_VORECrKA/s48-c-mo/photo.jpg",
                ChatConfig.STATUS_NOT_TYPING
        );

        FirebaseDatabaseManager.createOrUpdateChatroom(compChatRoom);

    }


}
