package com.hapramp.interfaces;

import com.hapramp.models.response.PostResponse;

/**
 * Created by Ankit on 11/18/2017.
 */

public interface VotePostCallback {
    void onPostVoted(PostResponse.Results result);
    void onPostVoteError();
}
