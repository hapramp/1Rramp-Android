package bxute.config;

import android.support.annotation.IntDef;

/**
 * Created by Ankit on 7/13/2017.
 */

public class MessageStatus {

    //for outgoing message
    public static final int STATUS_PENDING = -1;
    public static final int STATUS_SENT = 0;
    public static final int STATUS_DELIVERED = 1;
    public static final int STATUS_SEEN = 2;
    //for incomming message
    public static final int STATUS_RECEIVED = 3;
    public static final int STATUS_DELIVERY_CONFIRMED = 4;
    public static final int STATUS_SEEN_CONFIRMED = 5;

    @IntDef({STATUS_PENDING,STATUS_SENT,STATUS_DELIVERED,STATUS_SEEN,STATUS_RECEIVED,STATUS_DELIVERY_CONFIRMED,STATUS_SEEN_CONFIRMED})
    public @interface MessageType{}

}
