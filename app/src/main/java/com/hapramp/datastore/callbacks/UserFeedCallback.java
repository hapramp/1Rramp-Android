package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.Feed;

import java.util.List;

public interface UserFeedCallback {
  void onFeedsFetching();

  void onUserFeedsAvailable(List<Feed> communityModelList, boolean isFreshData, boolean isAppendable);

  void onUserFeedFetchError(String err);
}
