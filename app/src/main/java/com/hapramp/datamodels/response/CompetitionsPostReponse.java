package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CompetitionsPostReponse {

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
    @SerializedName("participant_count")
    public int participant_count;
    @SerializedName("posts")
    public List<Posts> posts;

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

    public static class Posts {
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
    }

    public CompetitionsPostReponse(int id, String name, String handle, String description, String logo_uri, int participant_count, List<Posts> posts) {
        this.id = id;
        this.name = name;
        this.handle = handle;
        this.description = description;
        this.logo_uri = logo_uri;
        this.participant_count = participant_count;
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo_uri() {
        return logo_uri;
    }

    public void setLogo_uri(String logo_uri) {
        this.logo_uri = logo_uri;
    }

    public int getParticipant_count() {
        return participant_count;
    }

    public void setParticipant_count(int participant_count) {
        this.participant_count = participant_count;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "CompetitionsPostReponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                ", description='" + description + '\'' +
                ", logo_uri='" + logo_uri + '\'' +
                ", participant_count=" + participant_count +
                ", posts=" + posts +
                '}';
    }

}
