package com.hapramp.chat.config;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import static com.hapramp.chat.config.RemoteDataType.TYPE_MESSAGE;
import static com.hapramp.chat.config.RemoteDataType.TYPE_RECEIVING;
import static com.hapramp.chat.config.RemoteDataType.TYPE_SEEN;
import static com.hapramp.chat.config.RemoteDataType.TYPE_TYPING;

/**
 * Created by Ankit on 7/22/2017.
 */

public class RemoteDataType {

    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_RECEIVING = "receiving";
    public static final String TYPE_SEEN = "seen";
    public static final String TYPE_TYPING = "typing";

    @StringDef({TYPE_MESSAGE,TYPE_RECEIVING,TYPE_SEEN,TYPE_TYPING})
    public @interface RemoteData{}

}
