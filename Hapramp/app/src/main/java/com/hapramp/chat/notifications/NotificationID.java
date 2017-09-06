package com.hapramp.chat.notifications;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ankit on 16-06-2017.
 */
public class NotificationID {
    private static final AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}