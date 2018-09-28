package com.hapramp.utils;

import com.hapramp.notification.model.BaseNotificationModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationSortUtils {
  public static void sortNotification(List<BaseNotificationModel> notificationModels) {
    Collections.sort(notificationModels, new Comparator<BaseNotificationModel>() {
      @Override
      public int compare(BaseNotificationModel notification1, BaseNotificationModel notification2) {
        return notification1.getNotificationId().compareTo(notification2.getNotificationId());
      }
    });
  }
}
