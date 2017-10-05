package com.hapramp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bxute.activity.ChatRoomActivity;
import bxute.config.ChatConfig;
import bxute.config.Constants;
import bxute.config.MessageStatus;
import bxute.config.UserPreference;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.ChatRoom;
import bxute.models.Message;

public class DatabaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        injectData();
        test();
    }

    private void test(){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(Constants.EXTRAA_CHAT_ROOM_COMPANION_ID,"raj123");
        startActivity(intent);
    }

    private void injectData(){
        String MyId = UserPreference.getUserId();
        String compId = "raj123";

        Message message = new Message(ChatConfig.getMessageID(compId,MyId),
                "Message from Rajat",
                "08:00 AM",
                "",
                "",
                MessageStatus.STATUS_SENT,
                ChatConfig.getChatRoomId(compId,MyId),
                compId,MyId
        );

        ChatRoom myChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(compId,MyId),
                compId,
                "Ankit",
                message,
                0,
                0,
                "--no--avatar",
                "Online"
        );
        // get self node and add message
        FirebaseDatabaseManager.addMessageToSelfNode(message);
        FirebaseDatabaseManager.createOrUpdateChatroom(myChatRoom);

        // change message modo[change chat room id]
        message.setChatRoomId(ChatConfig.getChatRoomId(MyId,compId));
        message.setMessageId(ChatConfig.getMessageID(MyId,compId));

        ChatRoom compChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(MyId,compId),
                MyId,
                "Rajat",
                message,
                0,
                0,
                "--no--avatar",
                "Online"
        );

        FirebaseDatabaseManager.addMessageToRemoteNode(message);
        FirebaseDatabaseManager.createOrUpdateChatroom(compChatRoom);

    }

}
