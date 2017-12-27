package com.hapramp.interfaces;

/**
 * Created by Ankit on 12/27/2017.
 */

public interface MarkAsReadNotificationCallback {
    void onNotificationMarkedAsRead(int pos);
    void onNotificationMarkAsReadFailed();
}
