package bxute.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import bxute.ForegroundCheck;
import bxute.config.ChatConfig;
import bxute.config.HaprampTime;
import bxute.config.MessageStatus;
import bxute.config.RemoteDataType;
import bxute.database.DatabaseHelper;
import bxute.models.ChatRoom;
import bxute.models.Message;
import bxute.models.MessagePayloadModel;
import bxute.models.ReceivedStatusPayloadModel;
import bxute.models.SeenStatusPayloadModel;


/**
 * Created by ANKIT on 08-06-2017.
 */

public class FCMMessageService extends FirebaseMessagingService {

    private static final String TAG = FCMMessageService.class.getSimpleName();
    private ChatConfig chatConfig;
    private DatabaseHelper databaseHelper;
    private HaprampTime haprampTime;

    @Override
    public void onCreate() {
        super.onCreate();
        chatConfig = ChatConfig.getInstance();
        databaseHelper = new DatabaseHelper(this);
     //   notificationManager = HaprampNotificationManager.getInstance();
        haprampTime = HaprampTime.getInstance();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String type = remoteMessage.getData().get("contentType");
        String payload = remoteMessage.getData().get("Payload");
        switch (type) {
            case RemoteDataType.TYPE_MESSAGE:
                handleNewMessage(payload);
                break;
            case RemoteDataType.TYPE_RECEIVING:
                handleReceiving(payload);
                break;
            case RemoteDataType.TYPE_SEEN:
                handleSeen(payload);
                break;
            case RemoteDataType.TYPE_TYPING:
                handleTyping();
                break;
        }
    }

    /*
    *   method is called when someone is typing against this user.
    *   If app is foregrounded, it broadcast the event
    * */
    private void handleTyping() {

        if (!ForegroundCheck.isAppIsInBackground(this)) {
            // TODO: 7/23/2017  send broadcasts for typing...
        }

    }

    /*
    *   when message sent by this user is seen(receiver has sent the confirmation for seen)
    *   It updates the database if message was not deleted before
    *   and Broadcast the event too for UI update
    * */
    private void handleSeen(String payload) {
        SeenStatusPayloadModel.Payload seenPayload = new Gson().fromJson(payload, SeenStatusPayloadModel.Payload.class);
        // get Message from database
        Message message = databaseHelper.getMessage(seenPayload.messageID);

        // check existence of message in databse
        if (message != null) {
            message.setSeen_time(seenPayload.seen_time);
            message.setStatus(MessageStatus.STATUS_SEEN);
            databaseHelper.updateMessage(message);

            if (!ForegroundCheck.isAppIsInBackground(this)) {
                // TODO: 7/23/2017  send broadcasts for seen
            }

        }
    }

    /*
    *   when message sent by this user is received(receiver has sent the confirmation for received)
    *   It updates the database if message was not deleted before
    *   and Broadcast the event too for UI update
    * */
    private void handleReceiving(String payload) {

        ReceivedStatusPayloadModel.Payload recevingPayload = new Gson().fromJson(payload, ReceivedStatusPayloadModel.Payload.class);
        // get Message from database
        Message message = databaseHelper.getMessage(recevingPayload.messageID);

        // check existence of message in databse
        if (message != null) {
            message.setDelivered_time(recevingPayload.received_time);
            message.setStatus(MessageStatus.STATUS_DELIVERED);
            databaseHelper.updateMessage(message);

            if (!ForegroundCheck.isAppIsInBackground(this)) {
                // TODO: 7/23/2017  send broadcasts for receiving
            }
        }

    }

    /*
    *   when new message arrives
    *   It creates new message and invalidate the order of chat rooms
    *   and broadcast the event for UI Update
    * */
    private void handleNewMessage(String payload) {

        MessagePayloadModel.Payload messagePayload = new Gson().fromJson(payload, MessagePayloadModel.Payload.class);
        // save to databse
        Message message = new Message(
                chatConfig.getMessageID(messagePayload.senderID),
                messagePayload.text,
                messagePayload.time,    // message sent time
                haprampTime.getTime(), // message received time
                "",                     // seen time
                MessageStatus.STATUS_RECEIVED,
                chatConfig.getChatRoomId(messagePayload.senderID)
        );

        // create or update chatroom
        int already_unread_count = databaseHelper.getUnSeenCount(chatConfig.getChatRoomId(messagePayload.senderID));
        ChatRoom chatRoom = new ChatRoom(
                chatConfig.getChatRoomId(messagePayload.senderID),
                messagePayload.senderName,
                message,
                already_unread_count + 1,       // new unreads
                1                               // priority of chat this room
        );

        databaseHelper.incrementChatRoomsPriority();
        if (databaseHelper.addChatRooms(chatRoom) == -1) {  // chat room already exists
            // update
            databaseHelper.updateChatRoom(chatRoom);
        } else {
            // insert new
            databaseHelper.addChatRooms(chatRoom);
        }

        // insert message
        databaseHelper.addMessage(message);

        if (ForegroundCheck.isAppIsInBackground(this)) {
            // TODO: 7/23/2017  generate notifications
           // notificationManager.generateNotification(message);
        } else {
            // TODO: 7/23/2017  send broadcasts
        }
    }

}
