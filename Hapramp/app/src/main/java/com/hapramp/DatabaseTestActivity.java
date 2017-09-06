package com.hapramp;

import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hapramp.chat.database.DatabaseHelper;
import com.hapramp.chat.models.ChatRoom;
import com.hapramp.chat.models.Message;
import com.hapramp.chat.models.MessagePayloadModel;
import com.hapramp.chat.models.ReceivedStatusPayloadModel;
import com.hapramp.chat.models.SeenStatusPayloadModel;
import com.hapramp.chat.models.TypingStatusPayloadModel;
import com.hapramp.logger.L;

import butterknife.ButterKnife;

public class DatabaseTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        final EditText chatRoomId = (EditText) findViewById(R.id.chatRoomId);
        final EditText msg = (EditText) findViewById(R.id.message);
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        (findViewById(R.id.add_message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int msgId = (int) SystemClock.currentThreadTimeMillis();
                databaseHelper.addChatRooms(new ChatRoom(chatRoomId.getText().toString(),"CrNameA",new Message(String.valueOf(msgId),msg.getText().toString(),"12:00","1:00","2:00",1,chatRoomId.getText().toString()),1,1));
                databaseHelper.addMessage(new Message(String.valueOf(msgId),msg.getText().toString(),"12:00","1:00","2:00",1,chatRoomId.getText().toString()));
            }
        });

        (findViewById(R.id.delete_chat_rooms)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteChatRoom(chatRoomId.getText().toString());
            }
        });

        findViewById(R.id.read_chat_rooms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.D.m("Database : ",databaseHelper.getMessages(chatRoomId.getText().toString()).toString());
            }
        });

        testGSON();

    }

    public void testGSON(){
        Gson gson = new Gson();

        MessagePayloadModel.Payload m_payload = new MessagePayloadModel.Payload("Hi","12:00","sdjfh2334jh34kh34","Ankit Kumar");
        MessagePayloadModel messagePayloadModel = new MessagePayloadModel("message",m_payload);
        String msgJson  = gson.toJson(messagePayloadModel,MessagePayloadModel.class);

        ReceivedStatusPayloadModel.Payload r_payload = new ReceivedStatusPayloadModel.Payload("msg_23_34","12:00");
        ReceivedStatusPayloadModel receivedStatusPayloadModel = new ReceivedStatusPayloadModel("rec",r_payload);
        String receivedJson = gson.toJson(receivedStatusPayloadModel,ReceivedStatusPayloadModel.class);

        SeenStatusPayloadModel.Payload s_payload = new SeenStatusPayloadModel.Payload("msg_123_3223","23:00");
        SeenStatusPayloadModel seenStatusPayloadModel = new SeenStatusPayloadModel("seen",s_payload);
        String seenJson = gson.toJson(seenStatusPayloadModel,SeenStatusPayloadModel.class);

        TypingStatusPayloadModel.Payload t_payload = new TypingStatusPayloadModel.Payload("1:00");
        TypingStatusPayloadModel typingStatusPayloadModel = new TypingStatusPayloadModel("typing",t_payload);
        String typingJson = gson.toJson(typingStatusPayloadModel,TypingStatusPayloadModel.class);

        String TAG = "TestAct";
        L.D.m(TAG,"Message = "+msgJson);
        L.D.m(TAG,"================================");
        L.D.m(TAG,"Received = "+receivedJson);
        L.D.m(TAG,"================================");
        L.D.m(TAG,"Seen = "+seenJson);
        L.D.m(TAG,"================================");
        L.D.m(TAG,"Typing = "+typingJson);
        L.D.m(TAG,"================================");

    }

}
