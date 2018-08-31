package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;
import com.hapramp.push.NotificationPayloadModel;

import java.util.List;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationResponse {


  @SerializedName("start")
  public int start;
  @SerializedName("limit")
  public int limit;
  @SerializedName("count")
  public int count;
  @SerializedName("next")
  public String next;
  @SerializedName("previous")
  public String previous;
  @SerializedName("results")
  public List<NotificationPayloadModel> results;

  public NotificationResponse(int start, int limit, int count, String next, String previous, List<NotificationPayloadModel> results) {
    this.start = start;
    this.limit = limit;
    this.count = count;
    this.next = next;
    this.previous = previous;
    this.results = results;
  }

  @Override
  public String toString() {
    return "NotificationResponse{" +
      "start=" + start +
      ", limit=" + limit +
      ", count=" + count +
      ", next='" + next + '\'' +
      ", previous='" + previous + '\'' +
      ", results=" + results +
      '}';
  }
}
