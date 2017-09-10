package bxute.models;

import bxute.config.MessageStatus;

/**
 * Created by Ankit on 7/13/2017.
 */

public class Message {
    public String messageId;
    public String content;
    public String sent_time;
    public String delivered_time;
    public String seen_time;
    @MessageStatus.MessageType
    public int status;
    public String chatRoomId;

    public Message(String messageId, String content, String sent_time, String delivered_time, String seen_time, int status, String chatRoomId) {
        this.messageId = messageId;
        this.content = content;
        this.sent_time = sent_time;
        this.delivered_time = delivered_time;
        this.seen_time = seen_time;
        this.status = status;
        this.chatRoomId = chatRoomId;
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
                '}';
    }
}
