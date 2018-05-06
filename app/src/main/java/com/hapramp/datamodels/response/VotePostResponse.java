package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/18/2017.
 */

public class VotePostResponse {

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
    @SerializedName("current_vote")
    public int current_vote;
    @SerializedName("comment_count")
    public int comment_count;
    @SerializedName("contest_post")
    public Contest_post contest_post;

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

    public static class Contest_post {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("logo_uri")
        public String logo_uri;
    }
}
