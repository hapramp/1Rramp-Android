package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostResponse {

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
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("skills")
    public List<Skills> skills;
    @SerializedName("vote_count")
    public int vote_count;
    @SerializedName("vote_sum")
    public int vote_sum;
    @SerializedName("comment_count")
    public int comment_count;

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

    public PostResponse(int id, String created_at, String content, String media_uri, int post_type, int user_id, List<Skills> skills, int vote_count, int vote_sum, int comment_count) {
        this.id = id;
        this.created_at = created_at;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.user_id = user_id;
        this.skills = skills;
        this.vote_count = vote_count;
        this.vote_sum = vote_sum;
        this.comment_count = comment_count;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", content='" + content + '\'' +
                ", media_uri='" + media_uri + '\'' +
                ", post_type=" + post_type +
                ", user_id=" + user_id +
                ", skills=" + skills +
                ", vote_count=" + vote_count +
                ", vote_sum=" + vote_sum +
                ", comment_count=" + comment_count +
                '}';
    }
}
