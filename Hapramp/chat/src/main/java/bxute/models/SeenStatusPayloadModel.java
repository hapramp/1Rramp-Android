package bxute.models;

import java.io.Serializable;

import bxute.config.RemoteDataType;

/**
 * Created by Ankit on 7/20/2017.
 */

public class SeenStatusPayloadModel implements Serializable{

    @RemoteDataType.RemoteData
    public String contentType;
    public Payload payload;

    public SeenStatusPayloadModel(@RemoteDataType.RemoteData String contentType, Payload payload) {
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "SeenStatusPayloadModel{" +
                "contentType='" + contentType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public static class Payload implements Serializable{

        public String messageID;
        public String seen_time;

        public Payload(String messageID, String seen_time) {
            this.messageID = messageID;
            this.seen_time = seen_time;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "messageID='" + messageID + '\'' +
                    ", seen_time='" + seen_time + '\'' +
                    '}';
        }
    }
}
