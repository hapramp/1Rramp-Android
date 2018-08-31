package com.hapramp.datastore;

import com.hapramp.datastore.callbacks.CommunitiesCallback;

import java.io.IOException;

import okhttp3.Response;

public class DataStore extends DataDispatcher {

  public void requestUserCommunities(final String username, final CommunitiesCallback communitiesCallback) {
    if (communitiesCallback != null) {
      communitiesCallback.onProcessing();
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
          Response response = NetworkApi.getNetworkApiInstance().fetchUserCommunities(url);
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
}
