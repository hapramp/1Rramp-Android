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
    public String contest_post;
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

    public static class Comments {
        @SerializedName("id")
        public int id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("content")
        public String content;
        @SerializedName("user_id")
        public int user_id;

        public Comments(int id, String created_at, String content, int user_id) {
            this.id = id;
            this.created_at = created_at;
            this.content = content;
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }


    public CommentsResponse(int id, String created_at, String content, String media_uri, int post_type, User user, List<Skills> skills, int vote_count, int vote_sum, boolean is_voted, int current_vote, int comment_count, String contest_post, List<Comments> comments) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia_uri() {
        return media_uri;
    }

    public void setMedia_uri(String media_uri) {
        this.media_uri = media_uri;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getVote_sum() {
        return vote_sum;
    }

    public void setVote_sum(int vote_sum) {
        this.vote_sum = vote_sum;
    }

    public boolean isIs_voted() {
        return is_voted;
    }

    public void setIs_voted(boolean is_voted) {
        this.is_voted = is_voted;
    }

    public int getCurrent_vote() {
        return current_vote;
    }

    public void setCurrent_vote(int current_vote) {
        this.current_vote = current_vote;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getContest_post() {
        return contest_post;
    }

    public void setContest_post(String contest_post) {
        this.contest_post = contest_post;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
