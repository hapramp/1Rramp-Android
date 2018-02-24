package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.internal.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class SteemFollowingRequestBody extends RequestBody {

    public static RequestBody create(String content) {
        MediaType contentType = MediaType.parse("application/plain");
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("application/plain");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

    }
}
