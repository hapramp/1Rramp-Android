package com.hapramp.datastore;

import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;

import java.io.IOException;

import okhttp3.Response;

public class DataStore extends DataDispatcher {

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

  public void requestUserFeed(final String username,
                              final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userFeedUrl(username);
        String cachedResponse = DataCache.get(url);
        if (cachedResponse != null) {
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
        catch (IOException e) {
          dispatchUserFeedsError("IOException " + e.toString(), userFeedCallback);
        }
      }
    }.start();
  }

  public void requestUserFeed(final String username, final String start_author,
                              final String start_permlink,
                              final UserFeedCallback userFeedCallback) {
    if (userFeedCallback != null) {
      userFeedCallback.onWeAreFetchingUserFeed();
    }
    new Thread() {
      @Override
      public void run() {
        String url = URLS.userFeedUrl(username, start_author, start_permlink);
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

  public void requestUserBlog(final String username, final UserFeedCallback userFeedCallback) {
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
            dispatchUserFeeds(res, true, false, userFeedCallback);
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
            dispatchUserProfile(res, true, userProfileCallback);
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

}
