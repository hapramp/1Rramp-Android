package com.hapramp.datastore;


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
import com.hapramp.preferences.HaprampPreferenceManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class DataStore extends DataDispatcher {

  private String currentFeedRequestTag;

  /**
   * @param communitiesCallback callback
   */
  public void requestAllCommunities(final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      communitiesCallback.onWhileWeAreFetchingCommunities();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.allCommunityUrl();
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
              dispatchUserCommunity(res, true, communitiesCallback);
            }
          } else {
            dispatchUserCommunityError("Error Code:" + response.code(), communitiesCallback);
          }
        }
        catch (IOException e) {
          dispatchUserCommunityError("IOException " + e.toString(), communitiesCallback);
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
      communitiesCallback.onWhileWeAreFetchingCommunities();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userCommunityUrl(username);
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
        catch (IOException e) {
          dispatchUserCommunityError("IOException " + e.toString(), communitiesCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userFeedUrl(username);
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null && isFeedRequestLive(rtag) && !refresh) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag)) {
              if (!refresh && !cachedDataReturned) {
                //no refresh requested but no cache found, so we de-respect the refresh-denial
                // and return the fresh data.
                dispatchUserFeeds(res, true, false, userFeedCallback);
                return;
              }
              if (refresh) {
                //want to refresh
                dispatchUserFeeds(res, true, false, userFeedCallback);
              }
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userBlogUrl(username);
        String cachedResponse = DataCache.get(url);
        boolean cachedDataReturned = false;
        if (cachedResponse != null) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
          cachedDataReturned = true;
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (!refresh && !cachedDataReturned) {
              // no refresh requested but no cache found, so we de-respect the refresh denial
              // and return the fresh data.
              dispatchUserFeeds(res, true, false, userFeedCallback);
              return;
            }
            if (refresh) {
              dispatchUserFeeds(res, true, false, userFeedCallback);
            }
          } else {
            dispatchUserFeedsError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userBlogUrl(username, start_author, start_permlink);
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
      userProfileCallback.onWeAreFetchingUserProfile();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userProfileUrl(username);
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
        catch (IOException e) {
          dispatchUserProfileFetchError("IOException " + e.toString(), userProfileCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userFeedUrl(username, start_author, start_permlink);
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.curationUrl(tag);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null && isFeedRequestLive(rtag) && !refresh) {
          dispatchCommunityFeed(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (isFeedRequestLive(rtag) && refresh) {
              dispatchCommunityFeed(res, true, false, userFeedCallback);
            }
          } else {
            dispatchCommunityFeedError("Error Code:" + response.code(), userFeedCallback);
          }
        }
        catch (IOException e) {
          dispatchCommunityFeedError("IOException " + e.toString(), userFeedCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.steemUrl();
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.steemUrl();
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
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
        String url = URLS.steemUrl();
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
        catch (IOException e) {
          dispatchFollowInfo("IOException " + e.toString(), followInfoCallback);
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
          String url = URLS.steemUrl();
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
        catch (IOException e) {
          dispatchUserSearchError("IOException " + e.toString(), userSearchCallback);
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
          String url = URLS.steemUrl();
          String requestBody = SteemRequestBody.transactionState(username);
          Response response = NetworkApi.getNetworkApiInstance().postAndFetch(url, requestBody);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchTransferHistory(res,username, transferHistoryCallback);
          } else {
            dispatchTransferHistoryError("Error Code:" + response.code(), transferHistoryCallback);
          }
        }
        catch (IOException e) {
          dispatchTransferHistoryError("IOException " + e.toString(), transferHistoryCallback);
        }
      }
    }.start();
  }

  public void requestComments(final String author,
                              final String permlink,
                              final CommentsCallback commentsCallback) {
    if (commentsCallback != null) {
      commentsCallback.whileWeAreFetchingComments();
    }
    new Thread() {
      @Override
      public void run() {
        try {
          String url = URLS.steemUrl();
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
        catch (IOException e) {
          dispatchCommentsFetchError("IOException " + e.toString(), commentsCallback);
        }
      }
    }.start();
  }

  public void requestWalletInfo(final String username,
                                final UserWalletCallback userWalletCallback) {
    if (userWalletCallback != null) {
      userWalletCallback.whileWeAreFetchingWalletData();
    }
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = URLS.steemUrl();
          String globalProps = SteemRequestBody.globalProperties();
          Response globalPropsResponse = NetworkApi.getNetworkApiInstance().postAndFetch(steemUrl,
            globalProps);
          String profileUrl = URLS.userProfileUrl(username);
          Response userReponse = NetworkApi.getNetworkApiInstance().fetch(profileUrl);
          if (globalPropsResponse.isSuccessful() && userReponse.isSuccessful()) {
            String globalePropsResponseJson = globalPropsResponse.body().string();
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

  public static void requestSyncLastPostCreationTime() {
    new Thread() {
      @Override
      public void run() {
        try {
          String url = URLS.userProfileUrl(
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
          String steemUrl = URLS.steemUrl();
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
   * @param username          fetch followings of @username
   * @param startFromUser     start offset for followers
   * @param followingsCallback callback
   */
  public void requestFollowings(final String username,
                                final String startFromUser,
                                final FollowingsCallback followingsCallback) {
    new Thread() {
      @Override
      public void run() {
        try {
          String steemUrl = URLS.steemUrl();
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

}

