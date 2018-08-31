package com.hapramp.datastore;

import android.os.Handler;

import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.models.CommunityModel;

import java.util.List;

public class DataDispatcher {
  private Handler handler;
  private JSONParser jsonParser;

  DataDispatcher() {
    handler = new Handler();
    jsonParser = new JSONParser();
  }

  void dispatchUserCommunity(String response, final boolean isFresh, final CommunitiesCallback communitiesCallback) {
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
}
