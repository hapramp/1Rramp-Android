package bxute.models;

/**
 * Created by Ankit on 7/13/2017.
 */

public class ChatRoom {

    private String chatRoomId;
    private String ownerId;
    private String chatRoomName;
    private Message lastMessage;
    private int unreadCount;
    private int priority;
    private String chatRoomAvatar;
    private String typingStatus;

    public ChatRoom(){

    }

    public ChatRoom(String chatRoomId, String ownerId, String chatRoomName, Message lastMessage, int unreadCount, int priority, String chatRoomAvatar, String onlineStatus) {
        this.chatRoomId = chatRoomId;
        this.ownerId = ownerId;
        this.chatRoomName = chatRoomName;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.priority = priority;
        this.chatRoomAvatar = chatRoomAvatar;
        this.typingStatus = onlineStatus;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getChatRoomAvatar() {
        return chatRoomAvatar;
    }

    public void setChatRoomAvatar(String chatRoomAvatar) {
        this.chatRoomAvatar = chatRoomAvatar;
    }

    public String getTypingStatus() {
        return typingStatus;
    }

    public void setTypingStatus(String typingStatus) {
        this.typingStatus = typingStatus;
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
                ", ownerId='" + ownerId + '\'' +
                ", chatRoomName='" + chatRoomName + '\'' +
                ", lastMessage=" + lastMessage +
                ", unreadCount=" + unreadCount +
                ", priority=" + priority +
                ", chatRoomAvatar='" + chatRoomAvatar + '\'' +
                ", typingStatus='" + typingStatus + '\'' +
                '}';
    }
}
