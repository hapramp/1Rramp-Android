package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/22/2017.
 */

public class UpdateUserResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("karma")
    public int karma;

    public UpdateUserResponse(int id, String username, String full_name, int karma) {
        this.id = id;
        this.username = username;
        this.full_name = full_name;
        this.karma = karma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    @Override
    public String toString() {
        return "UpdateUserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", full_name='" + full_name + '\'' +
                ", karma=" + karma +
                '}';
    }
}
