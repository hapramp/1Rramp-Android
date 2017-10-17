package bxute.activity;

import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import bxute.FontManager;
import bxute.adapters.ChatRoomRecyclerAdapter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.ChatConfig;
import bxute.config.Constants;
import bxute.config.LocalTimeManager;
import bxute.config.MessageStatus;
import bxute.config.UserPreference;
import bxute.fcm.FirebaseDatabaseManager;
import bxute.models.ChatRoom;
import bxute.models.Message;

public class ChatRoomActivity extends AppCompatActivity{

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
    TextView sendBtn;
    @BindView(R2.id.backBtn)
    TextView backBtn;
    private String mCompanionId;
    private String mChatRoomId;
    private ArrayList<Message> messages;
    private Message mTempMessage;
    private LocalTimeManager timeManager;
    private Typeface typeface;
    private ChatRoomRecyclerAdapter chatRoomRecyclerAdapter;
    private String TYPING_TEXT = "typing...";

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
        attachTypingListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUnreadCount();
    }

    private void fetchAndListenToolbarInfo() {

        FirebaseDatabaseManager.getIdPoolRef(mChatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getValue().toString();
                Log.d("__DEBUG", "key - " + key + " | chat room " + mChatRoomId);

                FirebaseDatabaseManager.getChatRoomAvatarRef(UserPreference.getUserId(), key)
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabaseManager.getIdPoolRef(mChatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getValue().toString();
                FirebaseDatabaseManager.getChatRoomNameRef(UserPreference.getUserId(), key)
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabaseManager.getUserOnlineRef(mCompanionId)
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

        FirebaseDatabaseManager.getIdPoolRef(mChatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getValue().toString();
                FirebaseDatabaseManager.getTypingInfoRef(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("__DEBUG", dataSnapshot.getValue().toString());
                        if (dataSnapshot.getValue().toString().equals("1")) {
                            onlineStatus.setText(TYPING_TEXT);
                        } else {
                            onlineStatus.setText("Online");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initObjects() {
        typeface = new FontManager(this).getDefault();
        backBtn.setTypeface(typeface);
        sendBtn.setTypeface(typeface);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        timeManager = LocalTimeManager.getInstance();
        mChatRoomId = ChatConfig.getChatRoomId(UserPreference.getUserId(), mCompanionId);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMessage()) {
                    prepareAndSendMessage();
                    msgInput.setText("");
                }
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
                            mTempMessage = d.getValue(Message.class);
                            messages.add(mTempMessage);
                            notifyMessageSeen(mTempMessage);
                            chatRoomRecyclerAdapter.notifyDataSetChanged();
                            chatsRecyclerView.smoothScrollToPosition(chatRoomRecyclerAdapter.getItemCount() - 1);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void notifyMessageSeen(Message msg) {

        if (msg.getStatus() == MessageStatus.STATUS_SEEN || msg.getStatus() == MessageStatus.STATUS_DELETED || msg.isOutgoingMessage())
            return;

        Log.d("__DEBUG", "[Updating Seen]-" + msg.getMessageId() + "[Status]-" + msg.getStatus());

        msg.setDelivered_time(LocalTimeManager.getInstance().getTime());
        msg.setSeen_time(LocalTimeManager.getInstance().getTime());
        msg.setStatus(MessageStatus.STATUS_SEEN);
        FirebaseDatabaseManager.updateMessageSeen(mCompanionId, msg);

    }

    private void collectCompanionInfo() {
        try {
            mCompanionId = getIntent().getExtras().getString(Constants.EXTRAA_CHAT_ROOM_COMPANION_ID);
        } catch (NullPointerException e) {
            Toast.makeText(this, "No User Id", Toast.LENGTH_LONG).show();
        }
    }

    private void attachTypingListener() {

        msgInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notifyTyping(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                notifyTyping(false);
            }
        });
    }

    private void notifyTyping(boolean isTyping) {
        String _text = isTyping ? ChatConfig.STATUS_TYPING : ChatConfig.STATUS_NOT_TYPING;
        FirebaseDatabaseManager.setTypingStatus(mCompanionId, _text);
    }

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
                LocalTimeManager.getInstance().getDateTime(),
                "",
                "",
                MessageStatus.STATUS_SENT,
                ChatConfig.getChatRoomId(MyId, compId),
                MyId, compId
        );

        FirebaseDatabaseManager.addMessage(message);

        ChatRoom myChatRoom = new ChatRoom(
                ChatConfig.getChatRoomId(MyId, compId),
                MyId,
                "Cr-Rajat",
                message,
                0,
                0,
                "https://lh3.googleusercontent.com/-i3eYqs47cXs/AAAAAAAAAAI/AAAAAAAAAAA/ACnBePbq5DiyG23GGHEvxhS1K14h5fvKnw/s48-c-mo/photo.jpg",
                "Online"
        );
        // get self node and add message
        FirebaseDatabaseManager.createOrUpdateChatroom(myChatRoom);

        // message`s chatroom invalidation
        message.setChatRoomId(ChatConfig.getChatRoomId(message.getReceiverId(), message.getSenderId()));
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
        FirebaseDatabaseManager.updateUnreadCount(mCompanionId, compChatRoom.getChatRoomId());

    }

    private void resetUnreadCount() {
        FirebaseDatabaseManager.resetUnreadCount(UserPreference.getUserId(), mChatRoomId);
    }


}
