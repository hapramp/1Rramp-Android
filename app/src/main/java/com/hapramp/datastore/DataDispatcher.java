package com.hapramp.datastore;

import android.os.Handler;

import com.google.gson.Gson;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.datastore.callbacks.FollowersCallback;
import com.hapramp.datastore.callbacks.FollowingsCallback;
import com.hapramp.datastore.callbacks.RewardFundMedianPriceCallback;
import com.hapramp.datastore.callbacks.SinglePostCallback;
import com.hapramp.datastore.callbacks.TransferHistoryCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.datastore.callbacks.UserVestedShareCallback;
import com.hapramp.datastore.callbacks.UserWalletCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.FollowCountInfo;
import com.hapramp.models.GlobalProperties;
import com.hapramp.models.UserSearchResponse;
import com.hapramp.models.VestedShareModel;
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

  void dispatchAllCommunity(String response, final boolean isFresh, final CommunitiesCallback communitiesCallback) {
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

  void dispatchExplorePosts(String response, final boolean isFreshData, final boolean isAppendable,
                            final UserFeedCallback userFeedCallback) {
    final List<Feed> feeds = jsonParser.parseExplorePosts(response);
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

  void dispatchCompetitionEligibility(String response) {
    if (response != null) {
      jsonParser.parseCompetitionEligibilityResponse(response);
    }
  }

  void dispatchUserProfile(final String response, final boolean isFresh, final UserProfileCallback userProfileCallback) {
    if (userProfileCallback != null) {
      final User user = jsonParser.parseRawUserJson(response);
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

  void dispatchUserSearchError(final String error, final UserSearchCallback userSearchCallback) {
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
      final User user = jsonParser.parseRawUserJson(userProfileJson);

      final double steemPowerOwned = SteemPowerCalc.calculateSteemPower(
        Double.valueOf(user.getVesting_share().split(" ")[0]),
        globalProperties.getResult().getTotal_vesting_fund_steem(),
        globalProperties.getResult().getTotal_vesting_shares());

      final double steemPowerDelegated = SteemPowerCalc.calculateSteemPower(
        Double.valueOf(user.getDelegated_vesting_shares().split(" ")[0]),
        globalProperties.getResult().getTotal_vesting_fund_steem(),
        globalProperties.getResult().getTotal_vesting_shares());

      final double steemPowerReceived = SteemPowerCalc.calculateSteemPower(
        Double.valueOf(user.getReceived_vesting_shares().split(" ")[0]),
        globalProperties.getResult().getTotal_vesting_fund_steem(),
        globalProperties.getResult().getTotal_vesting_shares());

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
            userWalletCallback.onUserSteemPower(String.format(Locale.US, "%.3f SP", steemPowerOwned),
              String.format(Locale.US, "%.3f SP", steemPowerDelegated),
              String.format(Locale.US, "%.3f SP", steemPowerReceived)
            );
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

  void dispatchRewardFundAndMedianHistory(String reward_fund_response, String price_median,
                                          final RewardFundMedianPriceCallback rewardFundMedianPriceCallback) {
    if (rewardFundMedianPriceCallback != null) {
      try {
        JSONObject rewardObject = new JSONObject(reward_fund_response);
        JSONObject priceObject = new JSONObject(price_median);
        JSONObject result = priceObject.getJSONObject("result");
        final String base = result.getString("base");
        final String quote = result.getString("quote");
        result = rewardObject.getJSONObject("result");
        final String reward_balance = result.getString("reward_balance");
        final String recent_claims = result.getString("recent_claims");
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              rewardFundMedianPriceCallback.onFeedHistoryDataAvailable(reward_balance, recent_claims, base, quote);
            }
          }
        );
      }
      catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  void dispatchMedianPriceError(final String error,
                                final RewardFundMedianPriceCallback rewardFundMedianPriceCallback) {
    if (rewardFundMedianPriceCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          rewardFundMedianPriceCallback.onError(error);
        }
      });
    }
  }

  void dispatchVestedShareData(String response, final UserVestedShareCallback vestedShareCallback) {
    if (vestedShareCallback != null) {
      final ArrayList<VestedShareModel> vestedShareModels = jsonParser.parseAllUsersVestedShare(response);
      handler.post(new Runnable() {
        @Override
        public void run() {
          vestedShareCallback.onUserAccountsVestedShareDataAvailable(vestedShareModels);
        }
      });
    }
  }

  void dispatchVestedShareError(final String err, final UserVestedShareCallback vestedShareCallback) {
    if (vestedShareCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          vestedShareCallback.onVestedShareDataError(err);
        }
      });
    }
  }

  void dispatchSinglePost(String singlePostResponse, final SinglePostCallback singlePostCallback) {
    if (singlePostCallback != null) {
      final Feed feed = jsonParser.parseSingleFeed(singlePostResponse);
      handler.post(new Runnable() {
        @Override
        public void run() {
          singlePostCallback.onPostFetched(feed);
        }
      });
    }
  }

  void dispatchSinglePostError(final String err, final SinglePostCallback singlePostCallback) {
    if (singlePostCallback != null) {
      handler.post(new Runnable() {
        @Override
        public void run() {
          singlePostCallback.onPostFetchError(err);
        }
      });
    }
  }

}
