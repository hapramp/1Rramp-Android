package com.hapramp.search;

import com.google.gson.Gson;
import com.hapramp.search.models.CommentModel;

import java.util.List;

public class CommentsSearchManager implements NetworkUtils.NetworkResponseCallback {
  NetworkUtils networkUtils;
  private CommentFetchCallback commentFetchCallback;

  public CommentsSearchManager() {
    networkUtils = new NetworkUtils();
    networkUtils.setNetworkResponseCallback(this);
  }

  public void requestCommentFor(String author, String permlink) {
    String url = "https://api.steemit.com";
    String method = "POST";
    String body = "{\"jsonrpc\":\"2.0\", \"method\":\"tags_api.get_content_replies\"," +
      " \"params\":{\"author\":\"" + author + "\", \"permlink\":\"" + permlink + "\"}," +
      " \"id\":1}";
    networkUtils.request(url, method, body);
  }

  @Override
  public void onResponse(String response) {
    CommentModel commentModel = new Gson().fromJson(response, CommentModel.class);
    if (commentFetchCallback != null) {
      commentFetchCallback.onCommentFetched(commentModel.getResult().getDiscussions());
    }
  }

  @Override
  public void onError(String e) {
    if (commentFetchCallback != null) {
      commentFetchCallback.onCommentFetchError(e);
    }
  }

  public void setCommentFetchCallback(CommentFetchCallback commentFetchCallback) {
    this.commentFetchCallback = commentFetchCallback;
  }

  public interface CommentFetchCallback {
    void onCommentFetched(List<CommentModel.Discussions> commentModels);

    void onCommentFetchError(String e);
  }
}
