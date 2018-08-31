package com.hapramp.models.error;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/22/2017.
 */

public class GeneralErrorModel {

  @SerializedName("code")
  public int code;
  @SerializedName("message")
  public String message;
  @SerializedName("status")
  public String status;
  @SerializedName("field")
  public String field;

  public GeneralErrorModel(int code, String message, String status, String field) {
    this.code = code;
    this.message = message;
    this.status = status;
    this.field = field;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Override
  public String toString() {
    return "CreateUserError{" +
      "code=" + code +
      ", message='" + message + '\'' +
      ", status='" + status + '\'' +
      ", field='" + field + '\'' +
      '}';
  }
}
