package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostResponse {


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
        @SerializedName("karma")
        public int karma;
        @SerializedName("image_uri")
        public String image_uri;
    }

    public static class Skills {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;
    }

    public static class Skill {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;
    }

    public static class Organization {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("username")
        public String username;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;
    }

    public static class Club {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("skill")
        public Skill skill;
        @SerializedName("organization")
        public Organization organization;
    }

    public static class Contest_post {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("start_time")
        public String start_time;
        @SerializedName("end_time")
        public String end_time;
        @SerializedName("entry_fee")
        public int entry_fee;
        @SerializedName("give_out_ratio")
        public int give_out_ratio;
        @SerializedName("club")
        public Club club;
        @SerializedName("participant_count")
        public int participant_count;
    }

    public static class Results {
        @SerializedName("id")
        public int id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("content")
        public String content;
        @SerializedName("media_uri")
        public String media_uri;
        @SerializedName("post_type")
        public int post_type;
        @SerializedName("user")
        public User user;
        @SerializedName("skills")
        public List<Skills> skills;
        @SerializedName("vote_count")
        public int vote_count;
        @SerializedName("vote_sum")
        public int vote_sum;
        @SerializedName("is_voted")
        public boolean is_voted;
        @SerializedName("hapcoins")
        public float hapcoins;
        @SerializedName("current_vote")
        public int current_vote;
        @SerializedName("comment_count")
        public int comment_count;
        @SerializedName("contest_post")
        public Contest_post contest_post;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "start=" + start +
                ", limit=" + limit +
                ", count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }

}
