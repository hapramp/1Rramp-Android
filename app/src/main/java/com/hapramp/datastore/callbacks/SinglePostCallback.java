package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.Feed;

public interface SinglePostCallback {
  void onPostFetched(Feed feed);

  void onPostFetchError(String err);
}
