package bxute.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.adapters.ChatListRecyclerAdapter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.ChatConfig;
import bxute.database.DatabaseHelper;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.interfaces.ChatListitemClickListener;
import bxute.models.ChatRoom;


public class ChatListActivity extends AppCompatActivity implements ChatListitemClickListener {

    @BindView(R2.id.chat_list)
    RecyclerView chatList;
    ChatListRecyclerAdapter adapter;
    ArrayList<ChatRoom> chatRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        setAdapter();
        fetchChatRooms();
    }

    private void setAdapter() {

        chatRooms = new ArrayList<>();
        adapter = new ChatListRecyclerAdapter(this, this);
        adapter.setChatRooms(chatRooms);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatList.setAdapter(adapter);

    }

    private void fetchChatRooms() {


        FirebaseDatabaseManager.getChatRoomsRef()
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {   //datasnapshot represent childs of "chatRooms"
                chatRooms.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    chatRooms.add(d.getValue(ChatRoom.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void navigateToChatRoom(String userId) {
        Intent chatRoomIntent = new Intent(this, ChatRoomActivity.class);
        chatRoomIntent.putExtra("userId", userId);
        startActivity(chatRoomIntent);
    }

    @Override
    public void onItemClicked(String userId) {
        navigateToChatRoom(userId);
    }

}
