package bxute.fcm;

import android.util.Log;

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
        firebaseDatabase.setPersistenceEnabled(true);
        rootReference = firebaseDatabase.getReference("root");
        chatRoomsReference = rootReference.child("users");// get access to its own node
        chatsReference = rootReference.child("users");  // get access to its own node
        devicesReference = rootReference.child("devices");
        devicesReference.keepSynced(true);  // keeping device ids Synced fresh
        onlineStatusReference = rootReference.child("onlineStatus");


    }

    private static void i() {
        if (firebaseDatabase == null)
            init();
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        i();
        return firebaseDatabase;
    }

    public static DatabaseReference getRootReference() {
        i();
        return rootReference;
    }

    public static DatabaseReference getChatRoomsReferenceForFetching() {
        i();
        return chatRoomsReference.child(UserPreference.getUserId()).child("chatRooms");
    }

    /*
    * This method is used to update/create chat room for other companion
    * */
    public static DatabaseReference getChatRoomsReferenceForUpdatingOrCreate(String companionId) {
        i();
        return chatRoomsReference.child(companionId).child("chatRooms");
    }

    public static DatabaseReference getChatsReferenceForFetching() {
        i();
        return chatsReference.child(UserPreference.getUserId()).child("chats");
    }


    /*
    * This method is used to adding message to other user`s chat list
    * */
    public static DatabaseReference getChatsReferenceForSending(String companionId) {
        i();
        return chatsReference.child(companionId).child("chats");
    }

    public static DatabaseReference getDevicesReference() {
        i();
        return devicesReference;
    }

    public static DatabaseReference getOnlineStatusReference() {
        i();
        return onlineStatusReference;
    }

    /*
        * Used to register device itself on the server
        * */
    public static void registerDevice() {
        i();
        // Key<UserId>:Value<DeviceId>
        Log.d("DEBUG", "id " + FirebaseInstanceId.getInstance().getToken());
        devicesReference.child(UserPreference.getUserId()).setValue(FirebaseInstanceId.getInstance().getToken());
    }

    /*
    * Used to get Devices registered
    * */

    public static void getDevices() {
        i();
        devicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    L.D.m("DEBUG", d.getValue(String.class));
                }
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
    *   Used to add/update Chat Room of both sender and receiver
    *   Anybody who sends the message will perform update of both chat room
    * */
    public static void createOrUpdateChatroom(ChatRoom chatRoom) {
        i();
        getChatRoomsReferenceForUpdatingOrCreate(chatRoom.getOwnerId()).child(chatRoom.getChatRoomId()).setValue(chatRoom);
    }


    /**
     * Used to add/update message of both sender and receiver
     */
    public static void addMessageToSelfNode(Message message) {
        i();
        // for self node
        getChatsReferenceForSending(message.getSenderId()).child(message.getMessageId()).setValue(message);
    }


    /**
     * Used to add/update message of both sender and receiver
     */
    public static void addMessageToRemoteNode(Message message) {
        i();
        // for self node
        getChatsReferenceForSending(message.getReceiverId()).child(message.getMessageId()).setValue(message);
    }

}
