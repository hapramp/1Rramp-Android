package com.hapramp.chat.models;

import com.hapramp.chat.config.RemoteDataType;

import java.io.Serializable;

/**
 * Created by Ankit on 7/20/2017.
 */

public class ReceivedStatusPayloadModel implements Serializable{

    @RemoteDataType.RemoteData
    public String contentType;
    public Payload payload;

    public ReceivedStatusPayloadModel(@RemoteDataType.RemoteData String contentType, Payload payload) {
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "ReceivedStatusPayloadModel{" +
                "contentType='" + contentType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public static class Payload implements Serializable{

        public String messageID;
        public String received_time;

        public Payload(String messageID, String received_time) {
            this.messageID = messageID;
            this.received_time = received_time;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "messageID='" + messageID + '\'' +
                    ", received_time='" + received_time + '\'' +
                    '}';
        }
    }
}
