package com.hapramp.chat.models;

import java.io.Serializable;

/**
 * Created by Ankit on 7/20/2017.
 */

public class TypingStatusPayloadModel implements Serializable{

    public String contentType;
    public Payload payload;

    public TypingStatusPayloadModel(String contentType, Payload payload) {
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "TypingStatusPayloadModel{" +
                "contentType='" + contentType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public static class Payload implements Serializable{

        public String time;

        public Payload(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "time='" + time + '\'' +
                    '}';
        }
    }
}
