package bxute.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import bxute.config.Constants;
import bxute.config.MessageStatus;
import bxute.logger.L;
import bxute.models.ChatRoom;
import bxute.models.JobModel;
import bxute.models.Message;
import bxute.services.DispatchService;

import static bxute.database.DatabaseContract.DB_NAME;
import static bxute.database.DatabaseContract.DB_VERSION;

/**
 * Created by Ankit on 7/14/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper mInstance;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.E.m(TAG, "oncreate();");
        db.execSQL(TableStatement.CREATE_TABLE_CHATROOM);
        L.D.m(TAG, "chatrooms table created");
        db.execSQL(TableStatement.CREATE_TABLE_MESSAGES);
        L.D.m(TAG, "messages table created");

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(TableStatement.DROP_TABLE_CHAT_ROOMS);
        db.execSQL(TableStatement.CREATE_TABLE_MESSAGES);

        db.execSQL(TableStatement.CREATE_TABLE_MESSAGES);
        L.D.m(TAG, "messages table created");
        db.execSQL(TableStatement.CREATE_TABLE_CHATROOM);
        L.D.m(TAG, "chatrooms table created");
    }

    public ContentValues getMessageCVObject(Message msg) {

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MessageColumns.MESSAGE_ID, msg.messageId);
        values.put(DatabaseContract.MessageColumns.MESSAGE_CONTENT, msg.content);
        values.put(DatabaseContract.MessageColumns.MESSAGE_SENT_TIME, msg.sent_time);
        values.put(DatabaseContract.MessageColumns.MESSAGE_DELIVERED_TIME, msg.delivered_time);
        values.put(DatabaseContract.MessageColumns.MESSAGE_SEEN_TIME, msg.seen_time);
        values.put(DatabaseContract.MessageColumns.MESSAGE_STATUS, msg.status);
        values.put(DatabaseContract.MessageColumns.CHAT_ROOM_ID, msg.chatRoomId);
        return values;

    }

    public ContentValues getChatRoomCVObject(ChatRoom chatRoom) {

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ChatRoomColumns.CHAT_ROOM_ID, chatRoom.chatRoomId);
        values.put(DatabaseContract.ChatRoomColumns.CHAT_ROOM_NAME, chatRoom.chatRoomName);
        values.put(DatabaseContract.ChatRoomColumns.CHAT_ROOM_UNREAD_COUNT, chatRoom.unreadCount);
        values.put(DatabaseContract.ChatRoomColumns.CHAT_ROOM_PRIORITY, chatRoom.priority);
        values.put(DatabaseContract.ChatRoomColumns.CHAT_ROOM_LAST_MESSAGE_ID, chatRoom.lastMessage.messageId);
        return values;

    }

    public void addMessage(Message msg) {

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(DatabaseContract.TABLE_MESSAGE, null, getMessageCVObject(msg));
        if (id != -1) {
            L.D.m(TAG, "inserted Message");
        }

        if(msg.status==MessageStatus.STATUS_PENDING){
            informNewOutgoingMessage(msg.messageId);
        }

        if(msg.status== MessageStatus.STATUS_RECEIVED){
            informNewIncommingMessage(msg.messageId);
        }

    }

    public long addChatRooms(ChatRoom chatRoom) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(DatabaseContract.TABLE_CHAT_ROOM, null, getChatRoomCVObject(chatRoom));
        if (id != -1) {
            L.D.m(TAG, "inserted ChatRooms");
        }
        return id;
    }

    public Message getMessage(String messageId) {

        SQLiteDatabase db = getWritableDatabase();
        String selection = DatabaseContract.MessageColumns.MESSAGE_ID+"= ?";
        String[] selectionArgs = {messageId};
        Cursor cursor = db.query(DatabaseContract.TABLE_MESSAGE,
                DatabaseContract.messageProjection,
                selection,
                selectionArgs,
                null, null, null);

        cursor.moveToFirst();
        Message message = null;
        if (cursor.getCount() > 0) {

            message = new Message(
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SENT_TIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_DELIVERED_TIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SEEN_TIME)),
                    (int) cursor.getDouble(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_STATUS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.CHAT_ROOM_ID))
            );
        }
        return message;
    }

    public ArrayList<Message> getMessages(String chatRoomId) {

        SQLiteDatabase db = getWritableDatabase();
        String selection = DatabaseContract.MessageColumns.CHAT_ROOM_ID+"= ?";
        String[] selectionArgs = {chatRoomId};
        Cursor cursor = db.query(DatabaseContract.TABLE_MESSAGE,
                DatabaseContract.messageProjection,
                selection,
                selectionArgs,
                null, null, null);

        cursor.moveToFirst();
        ArrayList<Message> messages = new ArrayList<>();
        boolean hasNext = cursor.getCount() > 0;

        while (hasNext) {

            messages.add(new Message(
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SENT_TIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_DELIVERED_TIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SEEN_TIME)),
                    (int) cursor.getDouble(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_STATUS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.CHAT_ROOM_ID))
            ));

            hasNext = cursor.moveToNext();
        }

        return messages;

    }

    public ArrayList<ChatRoom> getChatRooms() {

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.TABLE_CHAT_ROOM,
                DatabaseContract.chatRoomProjection,
                null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        boolean hasNext = cursor.getCount() > 0;

        while (hasNext) {
            chatRooms.add(new ChatRoom(
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.ChatRoomColumns.CHAT_ROOM_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.ChatRoomColumns.CHAT_ROOM_NAME)),
                    getMessage(cursor.getString(cursor.getColumnIndex(DatabaseContract.ChatRoomColumns.CHAT_ROOM_LAST_MESSAGE_ID))),
                    (int) cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ChatRoomColumns.CHAT_ROOM_UNREAD_COUNT)),
                    (int) cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ChatRoomColumns.CHAT_ROOM_PRIORITY))
            ));
            hasNext = cursor.moveToNext();
        }

        return chatRooms;
    }

    public void updateChatRoom(ChatRoom newChatRoom) {
        SQLiteDatabase database = getWritableDatabase();
        long id = database.update(
                DatabaseContract.TABLE_CHAT_ROOM,
                getChatRoomCVObject(newChatRoom),
                DatabaseContract.ChatRoomColumns.CHAT_ROOM_ID+"= ?",
                new String[]{newChatRoom.chatRoomId});

        if (id > -1) {
            L.D.m(TAG, "updated Chatroom");
        }
    }

    public void updateMessage(Message newMessage) {

        SQLiteDatabase database = getWritableDatabase();
        long id = database.update(DatabaseContract.TABLE_MESSAGE,
                getMessageCVObject(newMessage),
                DatabaseContract.MessageColumns.MESSAGE_ID+"= ?",
                new String[]{newMessage.messageId});
        if (id > -1) {
            L.D.m(TAG, "updated message");
        }

        if(newMessage.status==MessageStatus.STATUS_DELIVERY_CONFIRMED){
            informMessageSeen(newMessage.messageId);
        }

    }

    public void deleteChatRoom(String chatRoomId) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(
                DatabaseContract.TABLE_CHAT_ROOM,
                DatabaseContract.ChatRoomColumns.CHAT_ROOM_ID+"= ?",
                new String[]{chatRoomId});
        if (id > -1) {
            L.D.m(TAG, "deleted chatroom");
        }
    }

    public void deleteMessage(String messageId) {

        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(DatabaseContract.TABLE_MESSAGE,
                DatabaseContract.MessageColumns.MESSAGE_ID+"= ?", new String[]{messageId});
        if (id > -1) {
            L.D.m(TAG, "deleted message");
        }

    }

    public void deleteAllMessage(String chatRoomId) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(DatabaseContract.TABLE_MESSAGE,
                DatabaseContract.MessageColumns.CHAT_ROOM_ID+"= ?", new String[]{chatRoomId});
        if (id > -1) {
            L.D.m(TAG, "deleted all messages");
        }
    }

    public ArrayList<JobModel> getJobs() {
        ArrayList<JobModel> jobs = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String selection = DatabaseContract.MessageColumns.MESSAGE_STATUS+
                "= ? OR "+
                DatabaseContract.MessageColumns.MESSAGE_STATUS+
                "= ? OR "+
                DatabaseContract.MessageColumns.MESSAGE_STATUS+
                "= ?";

        String[] selectionArgs = {
                String.valueOf(MessageStatus.STATUS_PENDING),   // message yet to send
                String.valueOf(MessageStatus.STATUS_RECEIVED),  // message delivery should be confirmed
                String.valueOf(MessageStatus.STATUS_DELIVERY_CONFIRMED)};   // message seen will be confirmed

        Cursor cursor = db.query(
                DatabaseContract.TABLE_MESSAGE,
                DatabaseContract.messageProjection,
                selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        Message message;
        boolean hasNext = cursor.getCount() > 0;

        while (hasNext) {

           message = new Message(
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_ID)),
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_CONTENT)),
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SENT_TIME)),
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_DELIVERED_TIME)),
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_SEEN_TIME)),
                   (int) cursor.getDouble(cursor.getColumnIndex(DatabaseContract.MessageColumns.MESSAGE_STATUS)),
                   cursor.getString(cursor.getColumnIndex(DatabaseContract.MessageColumns.CHAT_ROOM_ID))
           );

            int jobType = message.status==MessageStatus.STATUS_PENDING
                    ?
                    JobModel.JOB_TYPE_MESSAGE
                    :
                    (   message.status==MessageStatus.STATUS_RECEIVED
                            ?
                            JobModel.JOB_TYPE_RECEIVING
                            :
                            JobModel.JOB_TYPE_SEEN
                    );

            jobs.add(new JobModel(jobType,message.messageId));
            hasNext = cursor.moveToNext();
        }

        return jobs;
    }

    private void informNewOutgoingMessage(String msgId){

        Intent intent = new Intent(context,DispatchService.class);
        intent.setAction(Constants.ACTIONS.NEW_OUTGOING_MESSAGE);
        intent.putExtra("msgId",msgId);
        context.startService(intent);

    }

    private void informNewIncommingMessage(String msgId){

        Intent intent = new Intent(context,DispatchService.class);
        intent.setAction(Constants.ACTIONS.NEW_MESSAGE_RECEIVED);
        intent.putExtra("msgId",msgId);
        context.startService(intent);

    }

    private void informMessageSeen(String msgId){

        Intent intent = new Intent(context,DispatchService.class);
        intent.setAction(Constants.ACTIONS.MESSAGE_SEEN);
        intent.putExtra("msgId",msgId);
        context.startService(intent);

    }

    public int getUnSeenCount(String chatRoomId) {
        return 0;
    }

    public void incrementChatRoomsPriority() {
        SQLiteDatabase database = getWritableDatabase();
        String updateQuery = "UPDATE "+ DatabaseContract.TABLE_CHAT_ROOM +"SET "+ DatabaseContract.MessageColumns.MESSAGE_STATUS+ "="+ DatabaseContract.MessageColumns.MESSAGE_STATUS+" 1;";
        database.execSQL(updateQuery);
    }
}
