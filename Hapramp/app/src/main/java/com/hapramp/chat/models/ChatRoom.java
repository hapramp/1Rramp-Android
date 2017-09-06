package com.hapramp.chat.models;

/**
 * Created by Ankit on 7/13/2017.
 */

public class ChatRoom {

    public String chatRoomId;
    public String chatRoomName;
    public Message lastMessage;
    public int unreadCount;
    public int priority;

    public ChatRoom(String chatRoomId, String chatRoomName, Message lastMessage, int unreadCount, int priority) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.priority = priority;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {

        return "ChatRoom{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", chatRoomName='" + chatRoomName + '\'' +
                ", lastMessage=" + lastMessage +
                ", unreadCount=" + unreadCount +
                ", priority=" + priority +
                '}';
    }
}
