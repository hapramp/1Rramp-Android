package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/11/2017.
 */

public class CreateUserResponseModel {

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public String status;
    @SerializedName("field")
    public String field;
    @SerializedName("id")
    public int id;
    @SerializedName("email")
    public String email;
    @SerializedName("full_name")
    public String full_name;

    public CreateUserResponseModel(int code, String message, String status, String field, int id, String email, String full_name) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.field = field;
        this.id = id;
        this.email = email;
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "CreateUserResponseModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", field='" + field + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }
}
