package com.hapramp.interfaces;

import com.hapramp.models.response.CommentsResponse;
import com.hapramp.steem.models.Feed;

/**
 * Created by Ankit on 11/13/2017.
 */

public interface CommentFetchCallback {
    void onCommentFetched(CommentsResponse response);
    void onCommentFetchError();

    void onVoteDeleted(Feed updatedPost);
}
