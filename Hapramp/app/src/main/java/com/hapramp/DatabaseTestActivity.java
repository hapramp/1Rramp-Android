package com.hapramp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import bxute.config.MessageStatus;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.ChatRoom;
import bxute.models.Message;

public class DatabaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        testFCM();
    }

    private void testFCM(){
        Message m = new Message("msgid1","hi","sent_time","delivered_time","seen_time", MessageStatus.STATUS_PENDING,"cr_12_123");
        ChatRoom chatRoom = new ChatRoom("cr_12_123","Ankit Kumar",m,2,1);
        FirebaseDatabaseManager.registerDevice();
        FirebaseDatabaseManager.addChatRoom(chatRoom);
        FirebaseDatabaseManager.addMessage(m);
        FirebaseDatabaseManager.setOnlineStatus("Onlince");
    }
}
