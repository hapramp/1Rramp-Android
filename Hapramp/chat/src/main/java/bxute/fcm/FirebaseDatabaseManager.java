package bxute.fcm;

import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.util.Log;

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
import bxute.models.UserContactModel;

/**
 * Created by Ankit on 9/9/2017.
 */

public class FirebaseDatabaseManager {

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference rootReference;
    private static DatabaseReference usersNodeReference;
    private static DatabaseReference devicesNodeReference;
    private static DatabaseReference registeredUsersRef;
    private static DatabaseReference conversationRef;
    private static DatabaseReference idPoolRef;

    /*
    * Must be initalize before use
    * */
    private static void init() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        rootReference = firebaseDatabase.getReference(Node.ROOT);
        idPoolRef = rootReference.child(Node.ID_POOL);
        registeredUsersRef = rootReference.child(Node.REGISTERED_USERS);
        conversationRef = rootReference.child(Node.CONVERSATION);
        usersNodeReference = conversationRef.child(Node.USERS);// get access to its own node
        devicesNodeReference = rootReference.child(Node.DEVICES);
        devicesNodeReference.keepSynced(true);  // keeping device ids Synced fresh
    }

    private static void i() {
        if (firebaseDatabase == null)
            init();
    }

    private static FirebaseDatabase getFirebaseDatabase() {
        i();
        return firebaseDatabase;
    }

    private static DatabaseReference getRootReference() {
        i();
        return rootReference;
    }

    private static DatabaseReference getUserNodeRefByUserId(String userId) { // User Level Node
        i();
        return usersNodeReference.child(userId);
    }

    /*
    * Helper method to add self to registered Users [called Once the app is installed]
    * */
    public static void registerMe(UserContactModel contactModel){
        registeredUsersRef.child(contactModel.getUserId()).setValue(contactModel);
    }
    
    /*
    * Helper method for contacts ref
    * */
    public static DatabaseReference getContactRef(String userId) {
        return usersNodeReference.child(userId).child(Node.CONTACTS);
    }

    /*
    * Helper method for pending-in ref
    * */
    public static DatabaseReference getPendingInRef(String userId) {
        return usersNodeReference.child(userId).child(Node.PENDING_IN);
    }

    /*
    * Helper method for pending-in ref
    * */
    public static DatabaseReference getPendingOutRef(String userId) {
        return usersNodeReference.child(userId).child(Node.PENDING_OUT);
    }

    /*
    * Helper method for pending-in ref
    * */
    public static DatabaseReference getBlockedMeRef(String userId){
        return usersNodeReference.child(userId).child(Node.BLOCKED_ME);
    }

    /*
    * Helper method for pending-in ref
    * */
    public static DatabaseReference getMyBlackListRef(String userId){
        return usersNodeReference.child(userId).child(Node.MY_BLACKLIST);
    }



    /*
    * Helper method for reference to all chat rooms
    * */
    public static DatabaseReference getChatRoomsRef() {
        i();
        return getUserNodeRefByUserId(UserPreference.getUserId()).child(Node.CHATROOMS);
    }

    /*
    * Helper method for reference to a single chat room
    * */

    public static DatabaseReference getChatRoomRef(String userId, String chatRoomId) {
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATROOMS).child(chatRoomId);
    }

    /*
    * Helper methods for reference to all chats
    * */
    public static DatabaseReference getChatsReference(String userId, String chatRoomId) {
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATS).child(chatRoomId);
    }

    /*
    * Helper methods for reference to a single chat
    * */
    public static DatabaseReference getChatReference(String userId, String chatRoomId, String messageId) {
        i();
        return getUserNodeRefByUserId(userId).child(Node.CHATS).child(chatRoomId).child(messageId);
    }

    /*
     * Used to register device itself on the server
     * */
    public static void registerDevice(String userId) {
        i();
        devicesNodeReference
                .child(userId)
                .child(Node.DEVICE_ID)
                .setValue(FirebaseInstanceId.getInstance().getToken());
    }

    /*
    * Helper method to reference typing status
    * */
    public static DatabaseReference getTypingInfoRef(String chatRoomId) {
        return getChatRoomRef(UserPreference.getUserId(), chatRoomId)
                .child(Node.TYPING_STATUS);
    }

    /*
     * Helper method to set typing status
     * */
    public static void setTypingStatus(final String userId, final String status) {

        idPoolRef.child(ChatConfig.getChatRoomId(userId, UserPreference.getUserId()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getTypingStatusRef(userId, dataSnapshot.getValue().toString()).setValue(status);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    /*
     * Used to register device itself on the server
     * */
    public static void registerDevice() {
        i();
        devicesNodeReference
                .child(UserPreference.getUserId())
                .child(Node.DEVICE_ID)
                .setValue(FirebaseInstanceId.getInstance().getToken());
    }

    /*
    * Helper method to get online reference to User Online status
    * */

    public static DatabaseReference getUserOnlineRef(String userID) {
        i();
        return rootReference.child(Node.DEVICES).child(userID).child(Node.ONLINE_STATUS);
    }

    /*
    * Helper method to set Online status of User
    * */
    public static void setOnlineStatus(String userId, String status) {
        i();
        rootReference.child(Node.DEVICES).child(userId).child(Node.ONLINE_STATUS).setValue(status);
    }


    /*
    * Helper method to set Online status of self
    * */
    public static void setOnlineStatus(String status) {
        i();
        rootReference.child(Node.DEVICES).child(UserPreference.getUserId()).child(Node.ONLINE_STATUS).setValue(status);
    }

    /*
    *   Used to add/update Chat Room of both sender and receiver
    *   Anybody who sends the message will perform update of both chat room
    * */
    public static void createOrUpdateChatroom(final ChatRoom chatRoom) {
        i();
        idPoolRef.child(chatRoom.getChatRoomId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String key = dataSnapshot.getValue().toString();
                    Log.d("__DEBUG", "Up-Gen [ChatRoom] prev key..." + key);
                    getChatRoomRef(chatRoom.getOwnerId(), key).setValue(chatRoom);

                } else {

                    String key = rootReference.push().getKey();
                    Log.d("__DEBUG", "New-Gen [ChatRoom] new key..." + key);
                    getChatRoomRef(chatRoom.getOwnerId(), key).setValue(chatRoom);
                    setKeyIdPair(key, chatRoom.getChatRoomId());

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
        String key = rootReference.push().getKey();
        Log.d("__DEBUG", "Gen [Message] self :" + key);
        getChatsReference(message.getSenderId(), message.getChatRoomId()).child(key).setValue(message);
        setKeyIdPair(key, message.getMessageId());
    }


    /**
     * Used to add/update message of receiver
     */
    public static void addMessageToRemote(Message message) {
        i();
        String key = rootReference.push().getKey();
        Log.d("__DEBUG", "Gen [Message] remote :" + key);
        getChatsReference(message.getReceiverId(), message.getChatRoomId()).child(key).setValue(message);
        setKeyIdPair(key, message.getMessageId());
    }

    public static void addMessage(Message message) {
        i();
        String key = rootReference.push().getKey();
        Log.d("__DEBUG", "Gen [Message] Both :" + key);

        // add to self
        getChatsReference(message.getSenderId(), message.getChatRoomId()).child(key).setValue(message);

        // add to remote
        message.setChatRoomId(ChatConfig.getChatRoomId(message.getReceiverId(), message.getSenderId()));
        getChatsReference(message.getReceiverId(), message.getChatRoomId()).child(key).setValue(message);

        setKeyIdPair(key, message.getMessageId());
    }

    /*
    * Helper method to get ref to Chat Room Avatar
    * */
    public static DatabaseReference getChatRoomAvatarRef(String userId, String chatRoomId) {
        return getChatRoomRef(userId, chatRoomId)
                .child(Node.CHAT_ROOM_AVATAR);


    }

    public static DatabaseReference getTypingStatusRef(String userId, String key) {
        return getChatRoomRef(userId, key).child(Node.TYPING_STATUS);
    }

    /*
    * Helper method to get ref to Chat Room Name
    * */
    public static DatabaseReference getChatRoomNameRef(String userId, String chatRoomId) {
        return getChatRoomRef(userId, chatRoomId)
                .child(Node.CHAT_ROOM_NAME);
    }

    /*
   * Helper method to get ref to Online Status of a Chat Room
   * */
    public static DatabaseReference getChatRoomOnlineStatusRef(String userId, String chatRoomId) {
        return getChatRoomRef(userId, chatRoomId)
                .child(Node.ONLINE_STATUS);
    }

    public static void updateUnreadCount(final String mCompanionId, final String chatRoomId) {

        getChatRoomRef(mCompanionId, chatRoomId)
                .child(Node.UNREAD_COUNT)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // check for existence of chat room
                        if (dataSnapshot.exists()) {
                            Log.d("__DEBUG", "increment message " + mCompanionId + " cr - " + chatRoomId);
                            Long value = (Long) dataSnapshot.getValue() + 1;
                            getChatRoomRef(mCompanionId, chatRoomId)
                                    .child(Node.UNREAD_COUNT).setValue(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static void resetUnreadCount(final String userId, final String chatRoomId) {

        idPoolRef.child(chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getChatRoomRef(
                        userId,
                        dataSnapshot.getValue().toString())
                        .child(Node.UNREAD_COUNT)
                        .setValue(0);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void setKeyIdPair(String key, String id) {
        Log.d("__DEBUG", "Adding Key To IdPool:" + key);
        idPoolRef.child(id).setValue(key);
    }

    public static DatabaseReference getIdPoolRef(String id) {
        return idPoolRef.child(id);
    }

    public static void updateMessageSeen(final String mCompanionId, final Message msg) {
        // get the key of message
        idPoolRef.child(msg.getMessageId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String msg_id = dataSnapshot.getValue().toString();
                //update message of self
                getChatReference(UserPreference.getUserId()
                        , ChatConfig.getChatRoomId(UserPreference.getUserId(), mCompanionId)
                        , msg_id)
                        .setValue(msg);

                //update message to remote
                getChatReference(mCompanionId
                        , ChatConfig.getChatRoomId(mCompanionId, UserPreference.getUserId())
                        , msg_id)
                        .setValue(msg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void sendChatRequest(UserContactModel myContact,UserContactModel remoteUserContact){
        // i want to send req to remoteUserContact
        // add remote to self PO
        getPendingOutRef(myContact.getUserId()).child(remoteUserContact.getUserId()).setValue(remoteUserContact);
        // add self to remote`s PI
        getPendingInRef(remoteUserContact.getUserId()).child(myContact.getUserId()).setValue(myContact);

    }

    public static void acceptChatRequest(UserContactModel myContact,UserContactModel remoteContactModel){
        // i want to accept changes
        // add remote user to self`s contact
        getContactRef(myContact.getUserId()).child(remoteContactModel.getUserId()).setValue(remoteContactModel);
        getContactRef(remoteContactModel.getUserId()).child(myContact.getUserId()).setValue(myContact);
    }

    public static void blockUser(UserContactModel myContact,UserContactModel remoteContactModel){
        // myContact blocks remoteContactModel
        // add to my black list
        getMyBlackListRef(myContact.getUserId()).child(remoteContactModel.getUserId()).setValue(remoteContactModel);
        // add self to blockedMe of remote
        getBlockedMeRef(remoteContactModel.getUserId()).child(myContact.getUserId()).setValue(myContact);
    }

    public static class Node {

        public static final String ROOT = "root";
        public static final String USERS = "users";
        public static final String REGISTERED_USERS = "registered_users";
        public static final String CONVERSATION = "conversations";
        public static final String CONTACTS = "contacts";
        public static final String PENDING_IN = "pending_ins";
        public static final String PENDING_OUT = "pending_outs";
        public static final String BLOCKED_ME = "blocked_me";
        public static final String MY_BLACKLIST = "my_blacklist";
        public static final String DEVICES = "devices";
        public static final String DEVICE_ID = "device_id";
        public static final String ONLINE_STATUS = "onlineStatus";
        public static final String TYPING_STATUS = "typingStatus";
        public static final String CHATS = "chats";
        public static final String CHATROOMS = "chatRooms";
        public static final String LAST_MESSAGE = "lastMessage";
        public static final String CHAT_ROOM_AVATAR = "chatRoomAvatar";
        public static final String CHAT_ROOM_NAME = "chatRoomName";
        public static final String UNREAD_COUNT = "unreadCount";
        public static final String ID_POOL = "id_pool";
    }

}
