package bxute.fcm;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.ArrayList;
import java.util.Map;
import bxute.config.UserPreference;
import bxute.logger.L;
import bxute.models.ChatRoom;
import bxute.models.Message;

/**
 * Created by Ankit on 9/9/2017.
 */

public class FirebaseDatabaseManager {

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference rootReference;
    private static DatabaseReference chatRoomsReference;
    private static DatabaseReference chatsReference;
    private static DatabaseReference devicesReference;
    private static DatabaseReference onlineStatusReference;

    /*
    * Must be initalize before use
    * */
    public static void init() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference("root");
        chatRoomsReference = rootReference.child("users").child(UserPreference.getUserId()).child("chatRooms");  // get access to its own node
        chatsReference = rootReference.child("users").child(UserPreference.getUserId()).child("chats");  // get access to its own node
        devicesReference = rootReference.child("devices");
        onlineStatusReference = rootReference.child("onlineStatus");
        firebaseDatabase.setPersistenceEnabled(true);
        devicesReference.keepSynced(true);  // keeping device ids Synced fresh

    }

    private static void i() {
        if (firebaseDatabase == null)
            init();
    }

    /*
    * Used to register device itself on the server
    * */
    public static void registerDevice() {
        i();
        // Key<UserId>:Value<DeviceId>
        devicesReference.child(UserPreference.getUserId()).setValue(FirebaseInstanceId.getInstance().getToken());
    }

    /*
    * Used to get Devices registered
    * */

    public static void getDevices(){
        i();
        devicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                L.D.m("DEBUG", dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    * Used to set Online status
    * */
    public static void setOnlineStatus(String status) {
        i();
        // Key<UserId>:Value<status>
        onlineStatusReference.child(UserPreference.getUserId()).setValue(status);
    }

    /*
    * Used to get Online status
    * */
    public static void getOnlineStatus(final String companionId){
        onlineStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                L.D.m("DEBUG","online:"+dataSnapshot.getValue().toString());
                // TODO: 9/10/2017 get Online status and set to view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    *   Used to add/update Chat Room
    * */

    public static void addChatRoom(ChatRoom chatRoom) {
        i();
        chatRoomsReference.child(chatRoom.chatRoomId).setValue(chatRoom);
    }

    /*
    * Used to get all chat Rooms
    * */
    public static void getChatRooms() {
        i();
        final ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        chatRoomsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Map<String, ChatRoom> chatRoomMap = (Map<String, ChatRoom>) d.getValue();
                    L.D.m("DEBUG",chatRoomMap.get(d.getKey()).toString());
                    chatRooms.add(chatRoomMap.get(d.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Used to add Chat
     */
    public static void addMessage(Message message) {
        i();
        chatsReference.child(message.messageId).setValue(message);
    }

    /*
    * Used to get all chats
    * */
    public static void getMessages() {

        final ArrayList<Message> messages = new ArrayList<>();
        chatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Map<String, Message> messageMap = (Map<String, Message>) d.getValue();
                    L.D.m("DEBUG",messageMap.get(d.getKey()).toString());
                    messages.add(messageMap.get(d.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
