package com.hapramp.interfaces;

import com.hapramp.models.Feed;
import com.hapramp.models.response.PostResponse;

/**
 * Created by Ankit on 12/15/2017.
 */

public interface VoteDeleteCallback {
    void onVoteDeleted(Feed updatedPost);
    void onVoteDeleteError();
}
