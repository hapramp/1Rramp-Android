package com.hapramp.search;

import com.google.gson.Gson;
import com.hapramp.search.models.FollowingsResponse;

import java.util.ArrayList;

public class FollowingSearchManager implements NetworkUtils.NetworkResponseCallback {
  private NetworkUtils networkUtils;
  private FollowingSearchCallback followerSearchCallback;

  public FollowingSearchManager(FollowingSearchCallback followerSearchCallback) {
    this.followerSearchCallback = followerSearchCallback;
    networkUtils = new NetworkUtils();
    networkUtils.setNetworkResponseCallback(this);
  }

  public void requestFollowings(String user) {
    String url = "https://api.steemit.com";
    String method = "POST";
    String body = "{\"jsonrpc\":\"2.0\", \"method\":\"follow_api.get_following\"," +
      "\"params\":{\"account\":\"" + user + "\",\"type\":\"blog\",\"limit\":1000}," +
      "\"id\":1}";
    networkUtils.request(url, method, body);
  }

  @Override
  public void onResponse(String response) {
    FollowingsResponse followSearchResponse = new Gson().fromJson(response, FollowingsResponse.class);
    ArrayList<String> followings = new ArrayList<>();
    for (int i = 0; i < followSearchResponse.getResult().getFollowings().size(); i++) {
      followings.add(followSearchResponse.getResult().getFollowings().get(i).getFollowing());
    }
    if (followerSearchCallback != null) {
      followerSearchCallback.onFollowingResponse(followings);
    }
  }

  @Override
  public void onError(String e) {
    if (followerSearchCallback != null) {
      followerSearchCallback.onFollowingRequestError(e);
    }
  }

  public interface FollowingSearchCallback {
    void onFollowingResponse(ArrayList<String> follower);

    void onFollowingRequestError(String e);
  }
}
