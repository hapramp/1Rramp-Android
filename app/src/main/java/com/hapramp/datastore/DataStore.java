package com.hapramp.datastore;

import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;

import java.io.IOException;

import okhttp3.Response;

public class DataStore extends DataDispatcher {

  private String currentFeedRequestTag;

  public void requestAllCommunities(final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      communitiesCallback.onWhileWeAreFetchingCommunities();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.allCommunityUrl();
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null) {
          dispatchUserCommunity(cachedResponse, false, communitiesCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchUserCommunity(res, true, communitiesCallback);
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
        if (cachedResponse != null) {
          dispatchUserCommunity(cachedResponse, false, communitiesCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchUserCommunity(res, true, communitiesCallback);
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

  public void requestUserFeed(final String username, final boolean refresh,
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
        if (cachedResponse != null && isFeedRequestLive(rtag)) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            if (refresh && isFeedRequestLive(rtag)) {
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

  private boolean isFeedRequestLive(String requestTag) {
    return this.currentFeedRequestTag.equals(requestTag);
  }

  public void requestUserBlog(final String username, final boolean refresh, final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userBlogUrl(username);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null) {
          dispatchUserFeeds(cachedResponse, false, false, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
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

  public void requestUserBlog(final String username, final String start_author,
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
        if (cachedResponse != null) {
          dispatchUserFeeds(cachedResponse, false, true, userFeedCallback);
        }
        try {
          Response response = NetworkApi.getNetworkApiInstance().fetch(url);
          if (response.isSuccessful()) {
            String res = response.body().string();
            DataCache.cache(url, res);
            dispatchUserFeeds(res, true, true, userFeedCallback);
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

  public void requestUserProfile(final String username, final UserProfileCallback userProfileCallback) {
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
            if (cachedResponse.length() == 0) {
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

  public void requestUserFeed(final String username, final String start_author,
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
        if (cachedResponse != null && isFeedRequestLive(rtag)) {
          dispatchUserFeeds(cachedResponse, false, true, userFeedCallback);
        }
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestCommunityFeed(final String tag, final boolean refresh, final UserFeedCallback userFeedCallback) {
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
        if (cachedResponse != null && isFeedRequestLive(rtag)) {
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
}
