package com.hapramp.datastore;

import android.os.Handler;

import com.google.gson.Gson;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.datastore.callbacks.FollowersCallback;
import com.hapramp.datastore.callbacks.FollowingsCallback;
import com.hapramp.datastore.callbacks.TransferHistoryCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.datastore.callbacks.UserWalletCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.FollowCountInfo;
import com.hapramp.models.GlobalProperties;
import com.hapramp.models.UserSearchResponse;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.steem.models.User;
import com.hapramp.utils.SteemPowerCalc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataDispatcher {
  private Handler handler;
  private JSONParser jsonParser;

  DataDispatcher() {
    handler = new Handler();
    jsonParser = new JSONParser();
  }

  void dispatchAllCommunity(String response,final boolean isFresh,final CommunitiesCallback communitiesCallback){
    final List<CommunityModel> communities = jsonParser.parseAllCommunity(response);
    if (communitiesCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          communitiesCallback.onCommunitiesAvailable(communities, isFresh);
        }
      });
    }
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

  void dispatchWalletInfo(String globalPropsJson,
                          String userProfileJson,
                          final UserWalletCallback userWalletCallback) {
    if (userWalletCallback != null) {
      GlobalProperties globalProperties = new Gson().fromJson(globalPropsJson, GlobalProperties.class);
      final User user = jsonParser.parseUser(userProfileJson);
      final double steemPower = SteemPowerCalc.calculateSteemPower(
        user.getVesting_share(),
        globalProperties.getResult().getTotal_vesting_fund_steem(),
        globalProperties.getResult().getTotal_vesting_shares()
      );
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            userWalletCallback.onUser(user);
            userWalletCallback.onUserSteem(user.getBalance());
            userWalletCallback.onUserSteemDollar(user.getSbd_balance());
            userWalletCallback.onUserSavingSteem(user.getSavings_balance());
            userWalletCallback.onUserSavingSBD(user.getSavings_sbd_balance());
            userWalletCallback.onUserRewards(
              user.getSbdRewardBalance(),
              user.getSteemRewardBalance(),
              user.getVestsRewardBalance());
            userWalletCallback.onUserSteemPower(String.format(Locale.US, "%.3f SP", steemPower));
          }
        }
      );
    }
  }

  void dispatchWalletInfoFetchError(final String error, final UserWalletCallback userWalletCallback) {
    if (userWalletCallback != null) {
      handler.post(
        new Runnable() {
          @Override
          public void run() {
            userWalletCallback.onUserWalletDataError(error);
          }
        }
      );
    }
  }

  void dispatchFollowers(final String response, final FollowersCallback followersCallback) {
    if (followersCallback != null) {
      final ArrayList<String> followers = new ArrayList<>();
      try {
        JSONArray results = new JSONObject(response).getJSONArray("result");
        for (int i = 0; i < results.length(); i++) {
          followers.add(results.getJSONObject(i).getString("follower"));
        }
      }
      catch (JSONException e) {
      }
      handler.post(new Runnable() {
        @Override
        public void run() {
          followersCallback.onFollowersAvailable(followers);
        }
      });
    }
  }

  void dispatchFollowings(final String response, final FollowingsCallback followingsCallback) {
    if (followingsCallback != null) {
      final ArrayList<String> followings = new ArrayList<>();
      try {
        JSONArray results = new JSONObject(response).getJSONArray("result");
        for (int i = 0; i < results.length(); i++) {
          followings.add(results.getJSONObject(i).getString("following"));
        }
      }
      catch (JSONException e) {
      }
      handler.post(new Runnable() {
        @Override
        public void run() {
          followingsCallback.onFollowingsAvailable(followings);
        }
      });
    }
  }

  void dispatchFollowersFetchError(final String error, final FollowersCallback followersCallback) {
    if (followersCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          followersCallback.onFollowersFetchError(error);
        }
      });
    }
  }

  void dispatchFollowingsFetchError(final String error, final FollowingsCallback followingsCallback) {
    if (followingsCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          followingsCallback.onFollowingsFetchError(error);
        }
      });
    }
  }
}
