package com.hapramp;

import android.support.annotation.IntDef;

/**
 * Created by Ankit on 5/25/2017.
 */

public class Constants {

    public static final boolean DEBUG = true;

    public static class ACTIONS{
        public static final String NEW_OUTGOING_MESSAGE = "com.hapramp.action.outgoing_message";
        public static final String NEW_MESSAGE_RECEIVED = "com.hapramp.action.message_received";
        public static final String MESSAGE_SEEN = "com.hapramp.action.message_seen";
    }

}
