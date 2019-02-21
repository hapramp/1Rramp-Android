package com.hapramp.datastore;


import com.google.gson.Gson;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.CompetitionEntriesFetchCallback;
import com.hapramp.datastore.callbacks.CompetitionsListCallback;
import com.hapramp.datastore.callbacks.DelegationsCallback;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.datastore.callbacks.FollowersCallback;
import com.hapramp.datastore.callbacks.FollowingsCallback;
import com.hapramp.datastore.callbacks.ResourceCreditCallback;
import com.hapramp.datastore.callbacks.RewardFundMedianPriceCallback;
import com.hapramp.datastore.callbacks.SinglePostCallback;
import com.hapramp.datastore.callbacks.TransferHistoryCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.datastore.callbacks.UserVestedShareCallback;
import com.hapramp.datastore.callbacks.UserWalletCallback;
import com.hapramp.interfaces.RebloggedUserFetchCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Response;

public class DataStore extends DataDispatcher {

  private String currentFeedRequestTag;

  /*
   *   Fetch reblogged Users
   * */

  /**
   * Fetches all communities
   */
  public static void performAllCommunitySync() {
    new DataStore().requestAllCommunities(new CommunitiesCallback() {
      @Override
      public void onCommunityFetching() {
      }

      @Override
      public void onCommunitiesAvailable(List<CommunityModel> communities, boolean isFreshData) {
        for (int i = 0; i < communities.size(); i++) {
          CommunityModel cm = communities.get(i);
          HaprampPreferenceManager.getInstance().setCommunityTagToColorPair(cm.getmTag(), cm.getmColor());
          HaprampPreferenceManager.getInstance().setCommunityTagToNamePair(cm.getmTag(), cm.getmName());
        }
        HaprampPreferenceManager.getInstance()
          .saveAllCommunityListAsJson(new Gson()
            .toJson(new CommunityListWrapper(communities)));
      }

      @Override
      public void onCommunitiesFetchError(String err) {

      }
    });
  }

  /**
   * @param communitiesCallback callback
   */
  public void requestAllCommunities(final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      communitiesCallback.onCommunityFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.allCommunityUrl();
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null) {
          dispatchAllCommunity(cachedResponse, false, communitiesCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (!cachedDataReturned) {
              dispatchAllCommunity(res, true, communitiesCallback);
            }
          } else {
            dispatchUserCommunityError("Error Code:" + response.code(), communitiesCallback);
          }
        }
        catch (Exception e) {
          dispatchUserCommunityError("Exception " + e.toString(), communitiesCallback);
        }
      }
    }.start();
  }

  /**
   * Fetches last post creation time from Steem Blockchain.
   */
  public static void requestSyncLastPostCreationTime() {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.userProfileUrl(
            HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
          Response globalPropsResponse = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString = globalPropsResponse.body().string();
          JSONObject jsonObject = new JSONObject(responseString);
          String lastPost = jsonObject
            .getJSONObject("user")
            .getString("last_root_post");
          HaprampPreferenceManager.getInstance().setLastPostCreatedAt(lastPost);
        }
        catch (Exception e) {

        }
      }
    }.start();
  }

  public void fetchRebloggedUsers(
    final String reqTag,
    final String author,
    final String permlink,
    final RebloggedUserFetchCallback rebloggedUserFetchCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.steemUrl();
          String body = SteemRequestBody.getRebloggedByBody(author, permlink);
          Response rebloggedUsers = NetworkApi.getNetworkApiInstance().postAndFetch(url, body);
          String responseString = rebloggedUsers.body().string();
          dispatchRebloggedUsers(reqTag, responseString, rebloggedUserFetchCallback);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  public void requestDelegations(final String username, final DelegationsCallback delegationsCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.steemUrl();
          String rb = SteemRequestBody.getDelegationsListBody(username);
          Response delegationsResponse = NetworkApi.getNetworkApiInstance().postAndFetch(url, rb);
          String responseString = delegationsResponse.body().string();
          dispatchDelegationsList(responseString, delegationsCallback);
        }
        catch (Exception e) {
          e.printStackTrace();
          dispatchDelegationsList(null, delegationsCallback);
        }
      }
    }.start();
  }

  /**
   * fetches competitions list.
   *
   * @param competitionsListCallback callback to return results.
   */
  public void requestCompetitionLists(final CompetitionsListCallback competitionsListCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.competitionsListUrlAfterId("");
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString;
          responseString = response.body().string();
          dispatchCompetitionsList(responseString, competitionsListCallback, false);
        }
        catch (Exception e) {
          e.printStackTrace();
          dispatchCompetitionListFetchError(competitionsListCallback);
        }
      }
    }.start();
  }

  /**
   * fetches competitions list.
   *
   * @param competitionsListCallback callback to return results.
   */
  public void requestCompetitionLists(final String afterCompetitionId, final CompetitionsListCallback competitionsListCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.competitionsListUrlAfterId(afterCompetitionId);
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString;
          responseString = response.body().string();
          dispatchCompetitionsList(responseString, competitionsListCallback, true);
        }
        catch (Exception e) {
          e.printStackTrace();
          dispatchCompetitionListFetchError(competitionsListCallback);
        }
      }
    }.start();
  }

  public void requestRc(final String username, final ResourceCreditCallback resourceCreditCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.rcInfoUrl(username);
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString;
          responseString = response.body().string();
          dispatchRc(responseString, resourceCreditCallback);
        }
        catch (Exception e) {
          dispatchRc(null, resourceCreditCallback);
        }
      }
    }.start();
  }

  public void requestCompetitionEntries(final String competitionId, final CompetitionEntriesFetchCallback competitionEntriesFetchCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.competitionEntryUrl(competitionId);
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString;
          responseString = response.body().string();
          dispatchCompetitionEntries(responseString, competitionEntriesFetchCallback);
        }
        catch (Exception e) {
          e.printStackTrace();
          dispatchCompetitionEntriesError(competitionEntriesFetchCallback);
        }
      }
    }.start();
  }

  public void requestWinnersList(final String competitionId, final CompetitionEntriesFetchCallback competitionEntriesFetchCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.competitionWinnersUrl(competitionId);
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          String responseString;
          responseString = response.body().string();
          dispatchCompetitionEntries(responseString, competitionEntriesFetchCallback);
        }
        catch (Exception e) {
          e.printStackTrace();
          dispatchCompetitionEntriesError(competitionEntriesFetchCallback);
        }
      }
    }.start();
  }

  /**
   * @param username            communities for username
   * @param communitiesCallback callback
   */
  public void requestUserCommunities(final String username,
                                     final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      communitiesCallback.onCommunityFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userCommunityUrl(username);
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null) {
          dispatchUserCommunity(cachedResponse, false, communitiesCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (!cachedDataReturned) {
              dispatchUserCommunity(res, true, communitiesCallback);
            }
          } else {
            dispatchUserCommunityError("Error Code:" + response.code(), communitiesCallback);
          }
        }
        catch (Exception e) {
          dispatchUserCommunityError("Exception " + e.toString(), communitiesCallback);
        }
      }
    }.start();
  }

  /**
   * @param username         feed of username
   * @param refresh          Whether want to refresh data after fetched from network.
   *                         This has significant effect when user manually refresh the data
   *                         (He wants to skip cached version).
   * @param userFeedCallback callback
   */
  public void requestUserFeed(final String username,
                              final boolean refresh,
                              final UserFeedCallback userFeedCallback) {
    final String rtag = "user_feed_initial";
    this.currentFeedRequestTag = rtag;
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userFeedUrl(username);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && isFeedRequestLive(rtag) && !refresh) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              dispatchUserFeeds(res, true, false, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  /**
   * @param requestTag check whether requestTag is currentRequestTag or not.
   * @return true when given request tag in still the live request.
   */
  private boolean isFeedRequestLive(String requestTag) {
    return this.currentFeedRequestTag.equals(requestTag);
  }

  /**
   * @param username         blog for username
   * @param refresh          Whether want to refresh data after fetched from network.
   * @param userFeedCallback callback
   */
  public void requestUserBlog(final String username,
                              final boolean refresh,
                              final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userBlogUrl(username);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && !refresh) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchUserFeeds(res, true, false, userFeedCallback);
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  /**
   * @param username         blog of username
   * @param start_author     blog starting from author
   * @param start_permlink   blog starting from permlink
   * @param userFeedCallback callback
   */
  public void requestUserBlog(final String username,
                              final String start_author,
                              final String start_permlink,
                              final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userBlogUrl(username, start_author, start_permlink);
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null) {
          dispatchUserFeeds(cachedResponse, false, true, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (!cachedDataReturned) {
              dispatchUserFeeds(res, true, true, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  /**
   * @param username            profile of username
   * @param userProfileCallback callback
   */
  public void requestUserProfile(final String username,
                                 final UserProfileCallback userProfileCallback) {
    if (userProfileCallback != null) {
      userProfileCallback.onUserProfileFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userProfileUrl(username);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null) {
          dispatchUserProfile(cachedResponse, false, userProfileCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (cachedResponse == null) {
              dispatchUserProfile(res, true, userProfileCallback);
            }
          } else {
            dispatchUserProfileFetchError("Error Code:" + response.code(), userProfileCallback);
          }
        }
        catch (Exception e) {
          dispatchUserProfileFetchError("Exception " + e.toString(), userProfileCallback);
        }
      }
    }.start();
  }

  public void requestUserFeed(final String username,
                              final String start_author,
                              final String start_permlink,
                              final UserFeedCallback userFeedCallback) {
    final String rtag = "more_user_feed_" + start_author + "_" + start_permlink;
    this.currentFeedRequestTag = rtag;
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.userFeedUrl(username, start_author, start_permlink);
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null && isFeedRequestLive(rtag)) {
          dispatchUserFeeds(cachedResponse, false, true, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag) && !cachedDataReturned) {
              dispatchUserFeeds(res, true, true, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestCommunityFeed(final String tag,
                                   final boolean refresh,
                                   final UserFeedCallback userFeedCallback) {
    final String rtag = "community_feed_" + tag;
    this.currentFeedRequestTag = rtag;
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.curationUrl(tag);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && isFeedRequestLive(rtag) && !refresh) {
          dispatchCommunityFeed(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              dispatchCommunityFeed(res, true, false, userFeedCallback);
            }
          } else {
            dispatchCommunityFeedError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchCommunityFeedError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  /**
   * @param tag              posts with tag.
   * @param refresh          Whether want to refresh data after fetched from network.
   * @param userFeedCallback callback
   */
  public void requestPostsNewOn1Ramp(final String tag,
                                     final boolean refresh,
                                     final UserFeedCallback userFeedCallback) {
    final String rtag = "new_on_hapramp";
    this.currentFeedRequestTag = rtag;
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.steemUrl();
        String cacheKey = "steemit_response_tag";
        String cachedResponse = DataCache.get(cacheKey);
        boolean cachedDataReturned = false;
        if (cachedResponse != null && isFeedRequestLive(rtag) && !refresh) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          String requestBody = SteemRequestBody.discussionsByCreated(tag);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              if (!refresh && !cachedDataReturned) {
                //no refresh requested but no cache found, so we de-respect the refresh denial
                // and return the fresh data.
                dispatchSteemFeed(res, true, false, userFeedCallback);
                return;
              }
              if (refresh) {
                //want to refresh
                dispatchSteemFeed(res, true, false, userFeedCallback);
              }
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  /**
   * @param username           follow info of username
   * @param followInfoCallback callback
   */
  public void requestFollowInfo(final String username,
                                final FollowInfoCallback followInfoCallback) {
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.steemUrl();
        String cacheKey = "steemit_user_follow_info_" + username;
        String cachedResponse = DataCache.get(cacheKey);
        if (cachedResponse != null) {
          dispatchFollowInfo(cachedResponse, followInfoCallback);
        }
        try {
          String requestBody = SteemRequestBody.followCountBody(username);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchFollowInfo(res, followInfoCallback);
          } else {
            dispatchFollowInfoError("Error Code:" + response.code(), followInfoCallback);
          }
        }
        catch (Exception e) {
          dispatchFollowInfo("Exception " + e.toString(), followInfoCallback);
        }
      }
    }.start();
  }

  /**
   * @param searchTerm         search usernames matching with searchTerm
   * @param userSearchCallback callback
   */
  public void requestUsernames(final String searchTerm,
                               final UserSearchCallback userSearchCallback) {
    if (userSearchCallback != null) {
      userSearchCallback.onSearchingUsernames();
    }
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.steemUrl();
          String requestBody = SteemRequestBody.lookupAccounts(searchTerm);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchUserSearch(res, userSearchCallback);
          } else {
            dispatchUserSearchError("Error Code:" + response.code(), userSearchCallback);
          }
        }
        catch (Exception e) {
          dispatchUserSearchError("Exception " + e.toString(), userSearchCallback);
        }
      }
    }.start();
  }

  /**
   * @param username                transfer history of username.
   * @param transferHistoryCallback callback
   */
  public void requestTransferHistory(final String username,
                                     final TransferHistoryCallback transferHistoryCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.steemUrl();
          String requestBody = SteemRequestBody.transactionState(username);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchTransferHistory(res, username, transferHistoryCallback);
          } else {
            dispatchTransferHistoryError("Error Code:" + response.code(), transferHistoryCallback);
          }
        }
        catch (Exception e) {
          dispatchTransferHistoryError("Exception " + e.toString(), transferHistoryCallback);
        }
      }
    }.start();
  }

  /**
   * @param tag              posts with tag.
   * @param start_author     post start from author.
   * @param start_permlink   post start from permlink
   * @param userFeedCallback callback
   */
  public void requestPostsNewOn1Ramp(final String tag,
                                     final String start_author,
                                     final String start_permlink,
                                     final UserFeedCallback userFeedCallback) {
    final String rtag = "new_on_hapramp";
    this.currentFeedRequestTag = rtag;
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.steemUrl();
        String cacheKey = "steemit_response_more_" + start_author + "_" + start_permlink;
        String cachedResponse = DataCache.get(cacheKey);
        boolean cachedDataReturned = false;
        if (cachedResponse != null && isFeedRequestLive(rtag)) {
          dispatchUserFeeds(cachedResponse, false, true, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          String requestBody = SteemRequestBody.discussionsByCreated(tag, start_author, start_permlink);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag) && !cachedDataReturned) {
              dispatchSteemFeed(res, true, true, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestComments(final String author,
                              final String permlink,
                              final CommentsCallback commentsCallback) {
    if (commentsCallback != null) {
      commentsCallback.onCommentsFetching();
    }
    new Thread() {
      @Override
      public void run() {
        try {
          String url = UrlBuilder.steemUrl();
          String requestBody = SteemRequestBody.contentReplies(author, permlink);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            //DataCache.cache(url, res);
            dispatchComments(res, commentsCallback);
          } else {
            dispatchCommentsFetchError("Error Code:" + response.code(), commentsCallback);
          }
        }
        catch (Exception e) {
          dispatchCommentsFetchError("Exception " + e.toString(), commentsCallback);
        }
      }
    }.start();
  }

  /**
   * @param username          fetch followers of @username
   * @param startFromUser     start offset for followers
   * @param followersCallback callback
   */
  public void requestFollowers(final String username,
                               final String startFromUser,
                               final FollowersCallback followersCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String followersReqBody = SteemRequestBody.followersListBody(username, startFromUser);
          Response followersResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            followersReqBody);
          if (followersResponse.isSuccessful()) {
            String followersResponseJson = followersResponse.body().string();
            dispatchFollowers(followersResponseJson, followersCallback);
          } else {
            dispatchFollowersFetchError("Error Code :" + followersResponse.code(), followersCallback);
          }
        }
        catch (Exception e) {
          dispatchFollowersFetchError("Error " + e.toString(), followersCallback);
        }
      }
    }.start();
  }

  /**
   * @param username           fetch followings of @username
   * @param startFromUser      start offset for followers
   * @param followingsCallback callback
   */
  public void requestFollowings(final String username,
                                final String startFromUser,
                                final FollowingsCallback followingsCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String followingsReqBody = SteemRequestBody.followingsListBody(username, startFromUser);
          Response followingsResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            followingsReqBody);
          if (followingsResponse.isSuccessful()) {
            String followersResponseJson = followingsResponse.body().string();
            dispatchFollowings(followersResponseJson, followingsCallback);
          } else {
            dispatchFollowingsFetchError("Error Code :" + followingsResponse.code(), followingsCallback);
          }
        }
        catch (Exception e) {
          dispatchFollowingsFetchError("Error " + e.toString(), followingsCallback);
        }
      }
    }.start();
  }

  public void requestWalletInfo(final String username,
                                final UserWalletCallback userWalletCallback) {
    if (userWalletCallback != null) {
      userWalletCallback.onFetchingWalletInfo();
    }
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String globalProps = SteemRequestBody.globalProperties();
          Response globalPropsResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            globalProps);
          String profileUrl = UrlBuilder.userProfileUrl(username);
          Response userReponse = NetworkApi.getNetworkApiInstance().fetch(profileUrl);
          if (globalPropsResponse.isSuccessful() && userReponse.isSuccessful()) {
            String globalePropsResponseJson = globalPropsResponse.body().string();
            HaprampPreferenceManager.getInstance().saveGlobalPropsInfo(globalePropsResponseJson);
            String userResponseJson = userReponse.body().string();
            dispatchWalletInfo(globalePropsResponseJson, userResponseJson, userWalletCallback);
          } else {
            dispatchWalletInfoFetchError("Error Code:" +
              globalPropsResponse.code() +
              "& " +
              userReponse.code(), userWalletCallback);
          }
        }
        catch (Exception e) {
          dispatchWalletInfoFetchError("Error:" + e.toString(), userWalletCallback);
        }
      }
    }.start();
  }

  public void requestRewardFundAndMedianHistory(final RewardFundMedianPriceCallback rewardFundMedianPriceCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String globalProps = SteemRequestBody.rewardFund();
          String medianPriceHistory = SteemRequestBody.medianPriceHistory();
          Response rewardFundResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            globalProps);
          Response medianPriceResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            medianPriceHistory);
          if (rewardFundResponse.isSuccessful() && medianPriceResponse.isSuccessful()) {
            String rewardResponseJson = rewardFundResponse.body().string();
            String priceResponseJson = medianPriceResponse.body().string();
            dispatchRewardFundAndMedianHistory(rewardResponseJson, priceResponseJson,
              rewardFundMedianPriceCallback);
          }
        }
        catch (Exception e) {
          dispatchMedianPriceError("Error:" + e.toString(), rewardFundMedianPriceCallback);
        }
      }
    }.start();
  }

  public void requestUserAccounts(final String[] users,
                                  final UserVestedShareCallback vestedShareCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String globalProps = SteemRequestBody.userAccounts(users);
          Response vestedShareResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            globalProps);
          if (vestedShareResponse.isSuccessful()) {
            String vestedResponseJson = vestedShareResponse.body().string();
            dispatchVestedShareData(vestedResponseJson, vestedShareCallback);
          }
        }
        catch (Exception e) {
          dispatchVestedShareError("Error:" + e.toString(), vestedShareCallback);
        }
      }
    }.start();
  }

  /**
   * Fetches single post from steemit api.
   *
   * @param author             author of post/feed
   * @param permlink           permlink of post/feed
   * @param singlePostCallback callback for returning response
   */
  public void requestSingleFeed(final String author, final String permlink, final SinglePostCallback singlePostCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = UrlBuilder.steemUrl();
          String requestBody = SteemRequestBody.getSinglePostBody(author, permlink);
          Response singlePostResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl, requestBody);
          if (singlePostResponse.isSuccessful()) {
            String vestedResponseJson = singlePostResponse.body().string();
            dispatchSinglePost(vestedResponseJson, singlePostCallback);
          } else {
            dispatchSinglePostError("Error:" + singlePostResponse.body().toString(), singlePostCallback);
          }
        }
        catch (Exception e) {
          dispatchSinglePostError("Error:" + e.toString(), singlePostCallback);
        }
      }
    }.start();
  }

  public void requestExploreFeeds(final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    final String rtag = "user_explore_feeds_initial";
    this.currentFeedRequestTag = rtag;
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.explorePostsUrl();
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null) {
          dispatchPosts(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              dispatchPosts(res, true, false, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestExploreFeeds(final boolean refresh, final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onFeedsFetching();
    }
    final String rtag = "user_explore_feeds_initial_ref";
    this.currentFeedRequestTag = rtag;
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.explorePostsUrl();
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && !refresh) {
          dispatchPosts(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag) && refresh) {
              dispatchPosts(res, true, false, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestMicroCommunityPosts(final String tag,
                                         final String order,
                                         final boolean refresh,
                                         final UserFeedCallback userFeedCallback) {
    final String rtag = "micro_community_feeds_" + tag + "_" + order;
    this.currentFeedRequestTag = rtag;
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.microCommunityPostsUrl(tag, order, 10);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && !refresh) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              dispatchUserFeeds(res, true, false, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestMicroCommunityPosts(final String tag,
                                         final String order,
                                         final String start_author,
                                         final String start_permlink,
                                         final UserFeedCallback userFeedCallback) {
    final String rtag = String.format("micro_community_feeds_%s_%s_%s_%s", tag, order, start_author,
      start_permlink);
    this.currentFeedRequestTag = rtag;
    new Thread() {
      @Override
      public void run() {
        String url = UrlBuilder.microCommunityPostsUrl(tag, order, 10, start_author, start_permlink);
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              dispatchUserFeeds(res, true, true, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (Exception e) {
          dispatchUserFeedsError("Exception " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

}

