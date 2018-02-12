package com.hapramp.interfaces;

import com.hapramp.models.Feed;
import com.hapramp.models.response.PostResponse;

/**
 * Created by Ankit on 11/18/2017.
 */

public interface VotePostCallback {

    void onPostVoted(Feed result);
    void onPostVoteError();

}
