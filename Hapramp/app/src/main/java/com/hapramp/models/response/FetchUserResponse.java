package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/11/2017.
 */

public class FetchUserResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("email")
    public String email;
    @SerializedName("full_name")
    public String full_name;

    public FetchUserResponse(int id, String email, String full_name) {
        this.id = id;
        this.email = email;
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "FetchUserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }
}
