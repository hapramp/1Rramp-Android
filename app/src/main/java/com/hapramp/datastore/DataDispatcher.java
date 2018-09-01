package com.hapramp.datastore;

import android.os.Handler;

import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.user.User;

import java.util.List;

public class DataDispatcher {
  private Handler handler;
  private JSONParser jsonParser;

  DataDispatcher() {
    handler = new Handler();
    jsonParser = new JSONParser();
  }

  void dispatchUserCommunity(String response, final boolean isFresh,
                             final CommunitiesCallback communitiesCallback) {
    final List<CommunityModel> communities = jsonParser.parseUserCommunity(response);
    if (communitiesCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          communitiesCallback.onCommunitiesAvailable(communities, isFresh);
        }
      });
    }
  }

  void dispatchUserCommunityError(final String message, final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          communitiesCallback.onCommunitiesFetchError(message);
        }
      });
    }
  }

  void dispatchUserFeeds(String response, final boolean isFreshData, final boolean isAppendable,
                         final UserFeedCallback userFeedCallback) {
    final List<Feed> feeds = jsonParser.parseUserFeed(response);
    if (userFeedCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          userFeedCallback.onUserFeedsAvailable(feeds, isFreshData, isAppendable);
        }
      });
    }
  }

  void dispatchUserFeedsError(final String message, final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          userFeedCallback.onUserFeedFetchError(message);
        }
      });
    }
  }

  void dispatchUserProfile(final String response, final boolean isFresh, final UserProfileCallback userProfileCallback) {
    if (userProfileCallback != null) {
      final User user = jsonParser.parseUser(response);
      handler.post(new Runnable() {
        @Override
        public void run() {
          userProfileCallback.onUserProfileAvailable(user, isFresh);
        }
      });
    }
  }

  void dispatchUserProfileFetchError(final String message, final UserProfileCallback userProfileCallback) {
    if (userProfileCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          userProfileCallback.onUserProfileFetchError(message);
        }
      });
    }
  }
}
