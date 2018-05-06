package com.hapramp.interfaces;

import com.hapramp.datamodels.response.PostResponse;

/**
 * Created by Ankit on 10/25/2017.
 */

public interface PostFetchCallback {
    void onPostFetched(PostResponse postResponses);
    void onPostFetchError();
}
