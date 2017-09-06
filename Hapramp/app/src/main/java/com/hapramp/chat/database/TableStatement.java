package com.hapramp.chat.database;

/**
 * Created by Ankit on 7/14/2017.
 */

public class TableStatement {


    public static final String CREATE_TABLE_MESSAGES = "CREATE TABLE " +
            "IF NOT EXISTS MESSAGES (" +
            "message_id text unique," +
            "content text," +
            "sent_time text," +
            "delivered_time text," +
            "seen_time text," +
            "status integer," +
            "chatroom_id text," +
            " FOREIGN KEY ( chatroom_id ) REFERENCES CHATROOMS (chatroom_id) ON DELETE CASCADE);";

    public static final String CREATE_TABLE_CHATROOM = "CREATE TABLE " +
            "IF NOT EXISTS CHATROOMS (" +
            "chatroom_id text unique," +
            "chatroom_name text," +
            "unread_count integer," +
            "priority integer," +
            "last_message_id text);";

    public static final String DROP_TABLE_MESSAGES = "drop table messages;";
    public static final String DROP_TABLE_CHAT_ROOMS = "drop table chatrooms;";

}
