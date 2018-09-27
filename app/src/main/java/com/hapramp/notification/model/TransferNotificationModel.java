package com.hapramp.notification.model;

import com.google.gson.Gson;

public class TransferNotificationModel  extends BaseNotificationModel {
  public String type;
  public String sender;
  public String amount;
  public String memo;
  public String timestamp;

  public TransferNotificationModel(String type, String from, String amount, String memo, String timestamp) {
    this.type = type;
    this.sender = from;
    this.amount = amount;
    this.memo = memo;
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFrom() {
    return sender;
  }

  public void setFrom(String from) {
    this.sender = from;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
