package com.hapramp.chat.models;

import com.hapramp.chat.config.RemoteDataType;

import java.io.Serializable;

/**
 * Created by Ankit on 7/20/2017.
 */

public class MessagePayloadModel implements Serializable{

    @RemoteDataType.RemoteData
    public String contentType;
    public Payload payload;

    public MessagePayloadModel(@RemoteDataType.RemoteData String contentType, Payload payload) {
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override

    public String toString() {
        return "MessagePayloadModel{" +
                "contentType='" + contentType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public static class Payload implements Serializable{

        public String text;
        public String time;
        public String senderID;
        public String senderName;

        public Payload(String text, String time, String senderID, String senderName) {
            this.text = text;
            this.time = time;
            this.senderID = senderID;
            this.senderName = senderName;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "text='" + text + '\'' +
                    ", time='" + time + '\'' +
                    ", senderID='" + senderID + '\'' +
                    ", senderName='" + senderName + '\'' +
                    '}';
        }
    }
}
