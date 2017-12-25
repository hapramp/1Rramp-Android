package com.hapramp.interfaces;

/**
 * Created by Ankit on 11/2/2017.
 */

public interface PostCreateCallback {
    void onPostCreated(String jobId);
    void onPostCreateError(String jobId);
}
