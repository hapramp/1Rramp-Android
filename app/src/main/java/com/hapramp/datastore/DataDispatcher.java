package com.hapramp.datastore;

import android.os.Handler;

import com.google.gson.Gson;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.datastore.callbacks.TransferHistoryCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;
import com.hapramp.search.models.FollowCountInfo;
import com.hapramp.search.models.UserSearchResponse;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.steem.models.User;

import java.util.ArrayList;
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

  void dispatchUserCommunityError(final String message,
                                  final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          communitiesCallback.onCommunitiesFetchError(message);
        }
      });
    }
  }

  void dispatchSteemFeed(String response,
                         final boolean isFreshData,
                         final boolean isAppendable,
                         final UserFeedCallback userFeedCallback) {
    final List<Feed> feeds = jsonParser.parseSteemFeed(response);
    if (userFeedCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          userFeedCallback.onUserFeedsAvailable(feeds, isFreshData, isAppendable);
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

  void dispatchCommunityFeed(String response, final boolean isFreshData, final boolean isAppendable,
                             final UserFeedCallback userFeedCallback) {
    final List<Feed> feeds = jsonParser.parseCuratedFeed(response);
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

  void dispatchCommunityFeedError(final String message, final UserFeedCallback userFeedCallback) {
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

  void dispatchFollowInfo(final String response, final FollowInfoCallback followInfoCallback) {
    if (followInfoCallback != null) {
      final FollowCountInfo followCountInfo = new Gson().fromJson(response, FollowCountInfo.class);
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            followInfoCallback.onFollowInfoAvailable(followCountInfo.getResult().getFollower_count(),
              followCountInfo.getResult().getFollowing_count());
          }
        }
      );
    }
  }

  void dispatchFollowInfoError(final String error, final FollowInfoCallback followInfoCallback) {
    if (followInfoCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          followInfoCallback.onFollowInfoError(error);
        }
      });
    }
  }

  void dispatchUserSearch(String response, final UserSearchCallback userSearchCallback) {
    if (userSearchCallback != null) {
      final UserSearchResponse userSearchResponse = new Gson().fromJson(response, UserSearchResponse.class);
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            userSearchCallback.onUserSuggestionsAvailable(userSearchResponse.getResult());
          }
        }
      );
    }
  }

  void dispatchUserSearchError(final String error, final UserSearchCallback userSearchCallback){
    if (userSearchCallback != null) {
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            userSearchCallback.onUserSuggestionsError(error);
          }
        }
      );
    }
  }

  void dispatchTransferHistory(String response,
                               String username,
                               final TransferHistoryCallback transferHistoryCallback) {
    final TransferHistoryParser transferHistoryParser = new TransferHistoryParser();
    final ArrayList<TransferHistoryModel> transferHistory = transferHistoryParser
      .parseTransferHistory(response, username);
    handler.post(new Runnable() {
      @Override
      public void run() {
        transferHistoryCallback.onAccountTransferHistoryAvailable(transferHistory);
      }
    });
  }

  void dispatchTransferHistoryError(final String error, final TransferHistoryCallback transferHistoryCallback) {
    if (transferHistoryCallback != null) {
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            transferHistoryCallback.onAccountTransferHistoryError(error);
          }
        }
      );
    }
  }

  void dispatchComments(String response, final CommentsCallback commentsCallback) {
    if (commentsCallback != null) {
      final ArrayList<CommentModel> comments = jsonParser.parseComments(response);
      handler.post(new Runnable() {
        @Override
        public void run() {
          commentsCallback.onCommentsAvailable(comments);
        }
      });
    }
  }

  void dispatchCommentsFetchError(final String err, final CommentsCallback commentsCallback) {
    if (commentsCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          commentsCallback.onCommentsFetchError(err);
        }
      });
    }
  }
}
