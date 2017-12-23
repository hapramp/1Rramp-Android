package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsResponse {


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
    public List<Results> results;

    public static class User {
        @SerializedName("id")
        public int id;
        @SerializedName("username")
        public String username;
        @SerializedName("full_name")
        public String full_name;
        @SerializedName("image_uri")
        public String image_uri;
    }

    public static class Results {
        @SerializedName("id")
        public int id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("content")
        public String content;
        @SerializedName("is_voted")
        public boolean is_voted;
        @SerializedName("vote_count")
        public int vote_count;
        @SerializedName("user")
        public User user;
    }

    public CommentsResponse(int start, int limit, int count, String next, String previous, List<Results> results) {
        this.start = start;
        this.limit = limit;
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    @Override
    public String toString() {
        return "CommentsResponse{" +
                "start=" + start +
                ", limit=" + limit +
                ", count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }
}
