package com.hapramp.interfaces;

import com.hapramp.models.response.PostResponse;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public interface PostFetchCallback {
    void onPostFetched(List<PostResponse> postResponses);
    void onPostFetchError();
}
