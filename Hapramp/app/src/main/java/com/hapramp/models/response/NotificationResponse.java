package com.hapramp.models.response;

import android.app.Notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationResponse {


    @SerializedName("start")
    public int start;
    @SerializedName("limit")
    public int limit;
    @SerializedName("count")
    public int count;
    @SerializedName("next")
    public String next;
    @SerializedName("previous")
    public String previous;
    @SerializedName("results")
    public List<Notification> results;

    public static class Notification {
        @SerializedName("id")
        public int id;
        @SerializedName("content")
        public String content;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("actor_id")
        public int actor_id;
        @SerializedName("action")
        public String action;
        @SerializedName("arg1")
        public String arg1;
        @SerializedName("is_read")
        public boolean is_read;

        public Notification(int id, String content, String created_at, int user_id, int actor_id, String action, String arg1, boolean is_read) {
            this.id = id;
            this.content = content;
            this.created_at = created_at;
            this.user_id = user_id;
            this.actor_id = actor_id;
            this.action = action;
            this.arg1 = arg1;
            this.is_read = is_read;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "id=" + id +
                    ", content='" + content + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", user_id=" + user_id +
                    ", actor_id=" + actor_id +
                    ", action='" + action + '\'' +
                    ", arg1='" + arg1 + '\'' +
                    ", is_read=" + is_read +
                    '}';
        }
    }


    public NotificationResponse(int start, int limit, int count, String next, String previous, List<Notification> results) {
        this.start = start;
        this.limit = limit;
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "start=" + start +
                ", limit=" + limit +
                ", count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
