package com.hapramp.chat.database;

import android.net.Uri;

/**
 * Created by Ankit on 9/5/2017.
 */

public class DatabaseContract {

    public static final String DB_NAME = "haprampDB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_MESSAGE = "messages";
    public static final String TABLE_CHAT_ROOM = "chatRooms";

    public static final String[] messageProjection = {
            MessageColumns.CHAT_ROOM_ID,
            MessageColumns.MESSAGE_CONTENT,
            MessageColumns.MESSAGE_SENT_TIME,
            MessageColumns.MESSAGE_DELIVERED_TIME,
            MessageColumns.MESSAGE_SEEN_TIME,
            MessageColumns.MESSAGE_STATUS,
            MessageColumns.CHAT_ROOM_ID
    };

    public static final String[] chatRoomProjection = {
            ChatRoomColumns.CHAT_ROOM_ID,
            ChatRoomColumns.CHAT_ROOM_NAME,
            ChatRoomColumns.CHAT_ROOM_UNREAD_COUNT,
            ChatRoomColumns.CHAT_ROOM_PRIORITY,
            ChatRoomColumns.CHAT_ROOM_LAST_MESSAGE_ID
    };

    public class MessageColumns {
        public static final String MESSAGE_ID = "message_id";
        public static final String MESSAGE_CONTENT = "content";
        public static final String MESSAGE_SEEN_TIME = "seen_time";
        public static final String MESSAGE_SENT_TIME = "sent_time";
        public static final String MESSAGE_DELIVERED_TIME = "delivered_time";
        public static final String MESSAGE_STATUS = "status";
        public static final String CHAT_ROOM_ID = "chat_room_id";
    }

    public class ChatRoomColumns {
        public static final String CHAT_ROOM_ID = "chat_room_id";
        public static final String CHAT_ROOM_NAME = "chat_room_name";
        public static final String CHAT_ROOM_UNREAD_COUNT = "unread_count";
        public static final String CHAT_ROOM_PRIORITY = "priority";
        public static final String CHAT_ROOM_LAST_MESSAGE_ID = "last_message_id";
    }

}
