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
    HashMap<String, Integer> indexMap; // key:chatRoomId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        setAdapter();
        indexMap = new HashMap<>();
        fetchChatRooms();
        fetchOnlineStatus();
    }

    private void setAdapter() {

        chatRooms = new ArrayList<>();
        adapter = new ChatListRecyclerAdapter(this, this);
        adapter.setChatRooms(chatRooms);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatList.setAdapter(adapter);

    }

    private void fetchChatRooms() {


        FirebaseDatabaseManager.getChatRoomsReferenceForFetching().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {   //datasnapshot represent childs of "chatRooms"
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.d("__DEBUG", "putting into index");
                    indexMap.put(d.getKey(), chatRooms.size());     // key: chatRoomId
                    chatRooms.add(d.getValue(ChatRoom.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchOnlineStatus() {

        FirebaseDatabaseManager.getOnlineStatusReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    // Update the
                    //updateStatus(ChatConfig.getChatRoomId(,d.getKey()), d.getValue(String.class)); // key is userID
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateStatus(String userId, String onlineStatus) {

        Log.d("__DEBUG", "setting online status :" + userId + " - " + onlineStatus);
        try {
            Log.d("__DEBUG", indexMap.toString());
            if (indexMap.get(userId) != null) {
                chatRooms.get(indexMap.get(userId)).setOnlineStatus(onlineStatus);
            }
        } catch (Exception e) {
            Log.d("__DEBUG", e.toString());
        }

        adapter.notifyDataSetChanged();

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
