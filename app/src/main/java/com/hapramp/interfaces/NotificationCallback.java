package com.hapramp.interfaces;

import com.hapramp.models.response.NotificationResponse;

/**
 * Created by Ankit on 12/27/2017.
 */

public interface NotificationCallback {
  void onNotificationsFetched(NotificationResponse notificationResponse);

  void onNotificationFetchError();
}
