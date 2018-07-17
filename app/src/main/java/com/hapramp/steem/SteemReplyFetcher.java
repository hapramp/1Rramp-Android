package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;

import com.hapramp.search.CommentsSearchManager;
import com.hapramp.search.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 4/15/2018.
 */

public class SteemReplyFetcher implements CommentsSearchManager.CommentFetchCallback {
  private Handler mHandler;
  private CommentsSearchManager commentsSearchManager;
  private SteemReplyFetchCallback steemReplyFetchCallback;
  private Runnable replyFetchFailedCallback = new Runnable() {
    @Override
    public void run() {
      if (steemReplyFetchCallback != null) {
        steemReplyFetchCallback.onReplyFetchError();
      }
    }
  };

  public SteemReplyFetcher() {
    this.mHandler = new Handler();
    commentsSearchManager = new CommentsSearchManager();
    commentsSearchManager.setCommentFetchCallback(this);
  }

  @WorkerThread
  public void requestReplyForPost(final String authorOfPost, final String permlink) {
    if (steemReplyFetchCallback != null) {
      steemReplyFetchCallback.onReplyFetching();
    }
    commentsSearchManager.requestCommentFor(authorOfPost, permlink);
  }

  public void setSteemReplyFetchCallback(SteemReplyFetchCallback steemReplyFetchCallback) {
    this.steemReplyFetchCallback = steemReplyFetchCallback;
  }

  @Override
  public void onCommentFetched(List<CommentModel.Discussions> commentModels) {
    final List<SteemCommentModel> contentCommentModels = new ArrayList<>();
    for (int i = 0; i < commentModels.size(); i++) {
      CommentModel.Discussions discussion = commentModels.get(i);
      contentCommentModels.add(new SteemCommentModel(
        discussion.getAuthor(),
        discussion.getBody(),
        discussion.getCreated(),
        "https://steemitimages.com/u/" + discussion.getAuthor() + "/avatar/small"
      ));
    }
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        if (steemReplyFetchCallback != null) {
          steemReplyFetchCallback.onReplyFetched(contentCommentModels);
        }
      }
    });

  }

  @Override
  public void onCommentFetchError(String e) {
    mHandler.post(replyFetchFailedCallback);
  }

  public interface SteemReplyFetchCallback {

    void onReplyFetching();

    void onReplyFetched(List<SteemCommentModel> replies);

    void onReplyFetchError();
  }

}
