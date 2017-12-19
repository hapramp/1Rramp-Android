package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsResponse {


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
    @SerializedName("comments")
    public List<Comments> comments;

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

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", full_name='" + full_name + '\'' +
                    ", karma=" + karma +
                    ", image_uri='" + image_uri + '\'' +
                    '}';
        }
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

        @Override
        public String toString() {
            return "Contest_post{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", handle='" + handle + '\'' +
                    ", logo_uri='" + logo_uri + '\'' +
                    '}';
        }
    }

    public static class Comments {
        @SerializedName("id")
        public int id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("content")
        public String content;
        @SerializedName("user_id")
        public int user_id;

        @Override
        public String toString() {
            return "Comments{" +
                    "id=" + id +
                    ", created_at='" + created_at + '\'' +
                    ", content='" + content + '\'' +
                    ", user_id=" + user_id +
                    '}';
        }
    }

    public CommentsResponse(int id, String created_at, String content, String media_uri, int post_type, User user, List<Skills> skills, int vote_count, int vote_sum, boolean is_voted, int current_vote, int comment_count, Contest_post contest_post, List<Comments> comments) {
        this.id = id;
        this.created_at = created_at;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.user = user;
        this.skills = skills;
        this.vote_count = vote_count;
        this.vote_sum = vote_sum;
        this.is_voted = is_voted;
        this.current_vote = current_vote;
        this.comment_count = comment_count;
        this.contest_post = contest_post;
        this.comments = comments;



    }

    @Override
    public String toString() {
        return "CommentsResponse{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", content='" + content + '\'' +
                ", media_uri='" + media_uri + '\'' +
                ", post_type=" + post_type +
                ", user=" + user +
                ", skills=" + skills +
                ", vote_count=" + vote_count +
                ", vote_sum=" + vote_sum +
                ", is_voted=" + is_voted +
                ", current_vote=" + current_vote +
                ", comment_count=" + comment_count +
                ", contest_post=" + contest_post +
                ", comments=" + comments +
                '}';
    }
}
