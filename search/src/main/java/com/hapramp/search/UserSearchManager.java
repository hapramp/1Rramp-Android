package com.hapramp.search;

import com.google.gson.Gson;
import com.hapramp.search.models.UserSearchResponse;

import java.util.ArrayList;

/**
 * Created by Ankit on 3/9/2018.
 */

public class UserSearchManager implements NetworkUtils.NetworkResponseCallback {
  private final ArrayList<String> empty_suggestion;
  NetworkUtils networkUtils;
  private UserSearchListener searchListener;

  public UserSearchManager(UserSearchListener searchListener) {
    this.searchListener = searchListener;
    networkUtils = new NetworkUtils();
    empty_suggestion = new ArrayList<>();
    networkUtils.setNetworkResponseCallback(this);
  }

  public void requestSuggestionsFor(final String segment) {
    onSearching();
    onPrepared();
    //loop through characters
    int len = segment.length();
    if (len == 0) {
      onSearched(empty_suggestion);
      return;
    }
    String url = "https://api.steemit.com";
    String method = "POST";
    String body = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.lookup_accounts\", \"params\":[\"" + segment + "\",10], \"id\":1}";
    networkUtils.request(url, method, body);
  }

  private void onSearching() {
    if (searchListener != null) {
      searchListener.onSearching();
    }
  }

  private void onPrepared() {
    if (searchListener != null) {
      searchListener.onPrepared();
    }
  }

  private void onSearched(final ArrayList<String> suggs) {
    if (searchListener != null) {
      searchListener.onSearched(suggs);
    }
  }

  @Override
  public void onResponse(String response) {
    UserSearchResponse userSearchResponse = new Gson().fromJson(response, UserSearchResponse.class);
    onSearched((ArrayList<String>) userSearchResponse.getResult());
  }

  @Override
  public void onError(String e) {
    onSearched(empty_suggestion);
  }

  public interface UserSearchListener {
    void onPreparing();

    void onPrepared();

    void onSearching();

    void onSearched(ArrayList<String> suggestions);
  }
}
