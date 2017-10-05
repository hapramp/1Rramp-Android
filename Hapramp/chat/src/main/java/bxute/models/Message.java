package bxute.models;

import bxute.config.ChatConfig;
import bxute.config.MessageStatus;
import bxute.config.UserPreference;

/**
 * Created by Ankit on 7/13/2017.
 */

public class Message {
    private String messageId;
    private String content;
    private String sent_time;
    private String delivered_time;
    private String seen_time;
    @MessageStatus.MessageType
    private int status;
    private String chatRoomId;
    private String senderId;
    private String receiverId;

    public Message(){

    }

    public Message(String messageId, String content, String sent_time, String delivered_time, String seen_time, int status, String chatRoomId, String senderId, String receiverId) {
        this.messageId = messageId;
        this.content = content;
        this.sent_time = sent_time;
        this.delivered_time = delivered_time;
        this.seen_time = seen_time;
        this.status = status;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public boolean isIncommingMessage(){
        return UserPreference.getUserId().equals(receiverId);
    }

    public boolean isOutgoingMessage(){
        return UserPreference.getUserId().equals(senderId);
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public String getDelivered_time() {
        return delivered_time;
    }

    public void setDelivered_time(String delivered_time) {
        this.delivered_time = delivered_time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSeen_time() {
        return seen_time;
    }

    public void setSeen_time(String seen_time) {
        this.seen_time = seen_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", content='" + content + '\'' +
                ", sent_time='" + sent_time + '\'' +
                ", delivered_time='" + delivered_time + '\'' +
                ", seen_time='" + seen_time + '\'' +
                ", status=" + status +
                ", chatRoomId='" + chatRoomId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                '}';
    }

}
