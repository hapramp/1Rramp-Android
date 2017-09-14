package com.hapramp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bxute.activity.ChatListActivity;
import bxute.activity.ChatRoomActivity;
import bxute.config.*;
import bxute.config.Constants;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.ChatRoom;
import bxute.models.Message;

public class DatabaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        injectData(ChatConfig.getChatRoomId("123"));
        injectData(ChatConfig.getChatRoomId("124"));
        injectData(ChatConfig.getChatRoomId("125"));
        test();
    }

    private void test(){
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(Constants.EXTRAA_CHAT_ROOM_COMPANION_ID,"123");
        startActivity(intent);
    }

    private void injectData(String crid){
        Message m = new Message(ChatConfig.getMessageID("124"),"hi","sent_time","delivered_time","seen_time", MessageStatus.STATUS_PENDING,crid,UserPreference.getUserId(),"124");
        ChatRoom chatRoom = new ChatRoom(crid,"Ankit Kumar",m,2,1,"http://cutehotpics.com/wp-content/gallery/rakul-preet-singh/Rakul-Preet-Singh-Hot-Pics-1.jpg","Online");
        FirebaseDatabaseManager.registerDevice();
        FirebaseDatabaseManager.addChatRoom(chatRoom);
        FirebaseDatabaseManager.addMessage(m);
        FirebaseDatabaseManager.getOnlineStatusReference().child("123").setValue("Online");
        FirebaseDatabaseManager.getOnlineStatusReference().child("124").setValue("Last seen today at: 1:00 PM");
        FirebaseDatabaseManager.getOnlineStatusReference().child("125").setValue("Online");

    }

}
