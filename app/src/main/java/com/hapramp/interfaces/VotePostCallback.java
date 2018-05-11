package com.hapramp.interfaces;

import com.hapramp.steem.models.Feed;

/**
 * Created by Ankit on 11/18/2017.
 */

public interface VotePostCallback {

    void onPostVoted(Feed result);
    void onPostVoteError();

}
