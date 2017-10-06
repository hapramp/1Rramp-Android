package bxute.fcm;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import bxute.config.ChatConfig;
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
    private static DatabaseReference usersNodeReference;
    private static DatabaseReference chatsReference;
    private static DatabaseReference devicesReference;
    private static DatabaseReference onlineStatusReference;

    /*
    * Must be initalize before use
    * */
    public static void init() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        rootReference = firebaseDatabase.getReference(Node.ROOT);
        usersNodeReference = rootReference.child(Node.USERS);// get access to its own node
        devicesReference = rootReference.child(Node.DEVICES);
        devicesReference.keepSynced(true);  // keeping device ids Synced fresh
        onlineStatusReference = rootReference.child(Node.ONLINE_STATUS);


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

    public static DatabaseReference getUserNodeRefByUserId(String userId){ // User Level Node
        i();
        return usersNodeReference.child(userId);
    }

    /*
    * Helper method for reference to all chat rooms
    * */
    public static DatabaseReference getChatRoomsRef(){
        i();
        return getUserNodeRefByUserId(UserPreference.getUserId()).child(Node.CHATROOMS);
    }

    /*
    * Helper method for reference to a single chat room
    * */

    public static DatabaseReference getChatRoomRef(String userId, String chatRoomId){
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATROOMS).child(chatRoomId);
    }

    /*
    * Helper methods for reference to all chats
    * */
    public static DatabaseReference getChatsReference(String userId, String chatRoomId){
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATS).child(chatRoomId);
    }

    /*
    * Helper methods for reference to a single chat
    * */
    public static DatabaseReference getChatReference(String userId, String chatRoomId , String messageId){
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATS).child(chatRoomId).child(messageId);
    }

    /*
    * Helper methods for reference to all devices
    * */

    public static DatabaseReference getDeviceReference(String deviceId){
        i();
        return devicesReference.child(deviceId);
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
        devicesReference.child(UserPreference.getUserId()).setValue(FirebaseInstanceId.getInstance().getToken());
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
    public static void createOrUpdateChatroom(final ChatRoom chatRoom) {
        i();
        getChatRoomRef(chatRoom.getOwnerId(),chatRoom.getChatRoomId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //update existing
                    L.D.m("__DEBUG","updating...");
                    getChatRoomRef(chatRoom.getOwnerId(),chatRoom.getChatRoomId()).child(Node.LAST_MESSAGE).setValue(chatRoom.getLastMessage());
                }else{
                    // create one
                    L.D.m("__DEBUG","creating new...");
                    getChatRoomRef(chatRoom.getOwnerId(),chatRoom.getChatRoomId()).setValue(chatRoom);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Used to add/update message of sender
     */
    public static void addMessageToSelf(Message message) {
        i();
        getChatReference(message.getSenderId(),message.getChatRoomId(),message.getMessageId()).setValue(message);
    }


    /**
     * Used to add/update message of receiver
     */
    public static void addMessageToRemote(Message message) {
        i();
        getChatReference(message.getReceiverId(),message.getChatRoomId(),message.getMessageId()).setValue(message);
    }

    /*
    * Helper method to get ref to Chat Room Avatar
    * */
    public static DatabaseReference getChatRoomAvatarRef(String userId,String chatRoomId){
        return getChatRoomRef(userId,chatRoomId)
                .child(Node.CHAT_ROOM_AVATAR);
    }

    /*
    * Helper method to get ref to Chat Room Name
    * */
    public static DatabaseReference getChatRoomNameRef(String userId,String chatRoomId){
        return getChatRoomRef(userId,chatRoomId)
                .child(Node.CHAT_ROOM_NAME);
    }

    /*
   * Helper method to get ref to Online Status of a Chat Room
   * */
    public static DatabaseReference getChatRoomOnlineStatusRef(String userId,String chatRoomId){
        return getChatRoomRef(userId,chatRoomId)
                .child(Node.ONLINE_STATUS);
    }


    public static class Node{
        public static final String ROOT = "root";
        public static final String USERS = "users";
        public static final String DEVICES = "devices";
        public static final String ONLINE_STATUS = "onlineStatus";
        public static final String CHATS = "chats";
        public static final String CHATROOMS = "chatRooms";
        public static final String LAST_MESSAGE = "lastMessage";
        public static final String CHAT_ROOM_AVATAR = "chatRoomAvatar";
        public static final String CHAT_ROOM_NAME = "chatRoomName";
    }

}
