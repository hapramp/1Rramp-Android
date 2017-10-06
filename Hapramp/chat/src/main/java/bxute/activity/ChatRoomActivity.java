package bxute.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.adapters.ChatRoomRecyclerAdapter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.ChatConfig;
import bxute.config.Constants;
import bxute.config.LocalTimeManager;
import bxute.config.MessageStatus;
import bxute.config.UserPreference;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.logger.L;
import bxute.models.ChatRoom;
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
    @BindView(R2.id.msg_input)
    EditText msgInput;
    @BindView(R2.id.send_btn)
    ImageView sendBtn;
    private String mCompanionId;
    private String mChatRoomId;
    private ArrayList<Message> messages;
    private LocalTimeManager timeManager;
    private ChatRoomRecyclerAdapter chatRoomRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        collectCompanionInfo();
        initObjects();
        setAdapter();
        fetchAndListenChats();
        fetchAndListenToolbarInfo();
    }

    private void fetchAndListenToolbarInfo() {

        FirebaseDatabaseManager.getChatRoomAvatarRef(UserPreference.getUserId(), mChatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("__DEBUG", dataSnapshot.getValue().toString());
                        chatRoomAvatar.setImageURI(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabaseManager.getChatRoomNameRef(UserPreference.getUserId(), mChatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("__DEBUG", dataSnapshot.getValue().toString());
                        chatRoomTitle.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabaseManager.getChatRoomOnlineStatusRef(UserPreference.getUserId(), mChatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("__DEBUG", dataSnapshot.getValue().toString());
                        onlineStatus.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initObjects() {
        timeManager = LocalTimeManager.getInstance();
        mChatRoomId = ChatConfig.getChatRoomId(UserPreference.getUserId(), mCompanionId);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMessage())
                    prepareAndSendMessage();
            }
        });
    }

    private void setAdapter() {

        messages = new ArrayList<>();
        chatRoomRecyclerAdapter = new ChatRoomRecyclerAdapter(this);
        chatRoomRecyclerAdapter.setMessages(messages);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatsRecyclerView.setAdapter(chatRoomRecyclerAdapter);

    }

    private void fetchAndListenChats() {

        FirebaseDatabaseManager.getChatsReference(UserPreference.getUserId(), mChatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        messages.clear();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            L.D.m("__DEBUG",d.getValue().toString());
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

    private boolean isValidMessage() {
        return msgInput.getText().toString().trim().length() > 0;
    }

    private String getMessage() {
        // TODO: 10/4/2017 add message moderator and validations
        return msgInput.getText().toString();
    }

    private void prepareAndSendMessage() {

        String MyId = UserPreference.getUserId();
        String compId = mCompanionId;
        Message message = new Message(ChatConfig.getMessageID(MyId, compId),
                getMessage(),
                timeManager.getTime(),
                "",
                "",
                MessageStatus.STATUS_SENT,
                ChatConfig.getChatRoomId(MyId, compId),
                MyId, compId
        );


        ChatRoom myChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(MyId, compId),
                MyId,
                chatRoomTitle.getText().toString(),
                message,
                0,
                0,
                "https://lh3.googleusercontent.com/-i3eYqs47cXs/AAAAAAAAAAI/AAAAAAAAAAA/ACnBePbq5DiyG23GGHEvxhS1K14h5fvKnw/s48-c-mo/photo.jpg",
                "Online"
        );
        // get self node and add message
        FirebaseDatabaseManager.createOrUpdateChatroom(myChatRoom);
        FirebaseDatabaseManager.addMessageToSelf(message);


        // change message modo[change chat room id]
        message.setChatRoomId(ChatConfig.getChatRoomId(compId, MyId));
        message.setMessageId(ChatConfig.getMessageID(compId, MyId));

        ChatRoom compChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(compId, MyId),
                compId,
                "Ankit Kumar",
                message,
                0,
                0,
                "https://lh3.googleusercontent.com/-i3eYqs47cXs/AAAAAAAAAAI/AAAAAAAAAAA/ACnBePbq5DiyG23GGHEvxhS1K14h5fvKnw/s48-c-mo/photo.jpg",
                "Online"
        );

        FirebaseDatabaseManager.createOrUpdateChatroom(compChatRoom);
        FirebaseDatabaseManager.addMessageToRemote(message);


    }
}
