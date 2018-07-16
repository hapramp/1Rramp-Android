package com.hapramp.interfaces;

import com.hapramp.steem.models.Feed;

/**
 * Created by Ankit on 12/15/2017.
 */

public interface VoteDeleteCallback {
  void onVoteDeleted(Feed updatedPost);

  void onVoteDeleteError();
}
