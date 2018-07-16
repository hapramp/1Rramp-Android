package com.hapramp.search;

import com.google.gson.Gson;
import com.hapramp.search.models.FollowCountInfo;

public class FollowCountManager implements NetworkUtils.NetworkResponseCallback {

  private NetworkUtils networkUtils;
  private FollowCountCallback followCountCallback;

  public FollowCountManager(FollowCountCallback followCountCallback) {
    this.followCountCallback = followCountCallback;
    networkUtils = new NetworkUtils();
    networkUtils.setNetworkResponseCallback(this);
  }

  public void requestFollowInfo(String user) {
    String url = "https://api.steemit.com";
    String method = "POST";
    String body = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_follow_count\"," +
      " \"params\":[\"" + user + "\"]," +
      " \"id\":1}";
    networkUtils.request(url, method, body);
  }

  @Override
  public void onResponse(String response) {
    FollowCountInfo followCountInfo = new Gson().fromJson(response, FollowCountInfo.class);
    if (followCountCallback != null) {
      followCountCallback.onFollowInfo(followCountInfo.getResult().getFollower_count(),
        followCountInfo.getResult().getFollowing_count());
    }
  }

  @Override
  public void onError(String e) {
    if (followCountCallback != null) {
      followCountCallback.onFollowInfoError(e);
    }
  }

  public interface FollowCountCallback {
    void onFollowInfo(int follower, int followings);

    void onFollowInfoError(String e);
  }
}
