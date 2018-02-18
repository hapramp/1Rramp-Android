package com.hapramp.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.models.error.GeneralErrorModel;

/**
 * Created by Ankit on 2/17/2018.
 */

public class SteemLoginResponseModel {

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("field")
    public String field;
    @SerializedName("status")
    @Expose
    private String status;

    public SteemLoginResponseModel(int code, String message, String field, String status) {
        this.code = code;
        this.message = message;
        this.field = field;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
