package com.hapramp.datamodels.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentCreateResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("content")
    public String content;
    @SerializedName("user_id")
    public int user_id;

    public CommentCreateResponse(int id, String created_at, String content, int user_id) {
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

    @Override
    public String toString() {
        return "CommentCreateResponse{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", content='" + content + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
