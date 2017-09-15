package bxute.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.adapters.ChatRoomRecyclerAdapter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.Constants;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.Message;

public class ChatRoomActivity extends AppCompatActivity {

    @BindView(R2.id.chatRoomAvatar)
    SimpleDraweeView chatRoomAvatar;
    @BindView(R2.id.chatRoomTitle)
    TextView chatRoomTitle;
    @BindView(R2.id.online_status)
    TextView onlineStatus;
    @BindView(R2.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R2.id.chatsRecyclerView)
    RecyclerView chatsRecyclerView;
    private String mCompanionId;
    private ArrayList<Message> messages;
    private HashMap<String, Integer> indexMap;
    private ChatRoomRecyclerAdapter chatRoomRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        collectCompanionInfo();
        setAdapter();
        fetchMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setAdapter() {
        messages = new ArrayList<>();
        indexMap = new HashMap<>();
        chatRoomRecyclerAdapter = new ChatRoomRecyclerAdapter(this);
        chatRoomRecyclerAdapter.setMessages(messages);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatsRecyclerView.setAdapter(chatRoomRecyclerAdapter);
    }

    private void fetchMessages() {

        FirebaseDatabaseManager.getChatsReferenceForFetching().child(mCompanionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.d("__DEBUG", "putting into index");
                    indexMap.put(d.getKey(), messages.size()); // key: MessageId (Since we want update at message level)
                    messages.add(d.getValue(Message.class));
                    chatRoomRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void collectCompanionInfo() {
        try {
            mCompanionId = getIntent().getExtras().getString(Constants.EXTRAA_CHAT_ROOM_COMPANION_ID);
        } catch (NullPointerException e) {
            Toast.makeText(this, "No User Id", Toast.LENGTH_LONG).show();
        }
    }
// TODO: 9/15/2017 Add input box + add dispatchers + add Online status + add typing status + add notifications
}
