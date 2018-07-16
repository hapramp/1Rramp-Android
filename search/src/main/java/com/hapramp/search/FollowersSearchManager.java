package com.hapramp.search;

import com.google.gson.Gson;
import com.hapramp.search.models.FollowersResponse;

import java.util.ArrayList;

public class FollowersSearchManager implements NetworkUtils.NetworkResponseCallback {

  private NetworkUtils networkUtils;
  private FollowerSearchCallback followerSearchCallback;

  public FollowersSearchManager(FollowerSearchCallback followerSearchCallback) {
    this.followerSearchCallback = followerSearchCallback;
    networkUtils = new NetworkUtils();
    networkUtils.setNetworkResponseCallback(this);
  }

  public void requestFollowers(String user) {
    String url = "https://api.steemit.com";
    String method = "POST";
    String body = "{\"jsonrpc\":\"2.0\", \"method\":\"follow_api.get_followers\"," +
      " \"params\":{\"account\":\"" + user + "\",\"type\":\"blog\",\"limit\":1000}," +
      " \"id\":1}";
    networkUtils.request(url, method, body);
  }

  public void setFollowerSearchCallback(FollowerSearchCallback followerSearchCallback) {
    this.followerSearchCallback = followerSearchCallback;
  }

  @Override
  public void onResponse(String response) {
    FollowersResponse followersSearchResponse = new Gson().fromJson(response, FollowersResponse.class);
    ArrayList<String> follwers = new ArrayList<>();
    for (int i = 0; i < followersSearchResponse.getResult().getFollowers().size(); i++) {
      follwers.add(followersSearchResponse.getResult().getFollowers().get(i).getFollower());
    }
    if (followerSearchCallback != null) {
      followerSearchCallback.onFollowerResponse(follwers);
    }
  }

  @Override
  public void onError(String e) {
    if (followerSearchCallback != null) {
      followerSearchCallback.onFollowerRequestError(e);
    }
  }

  public interface FollowerSearchCallback {
    void onFollowerResponse(ArrayList<String> follower);

    void onFollowerRequestError(String e);
  }
}
