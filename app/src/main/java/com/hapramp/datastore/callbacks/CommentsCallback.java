package com.hapramp.datastore.callbacks;

import com.hapramp.models.CommentModel;

import java.util.ArrayList;

public interface CommentsCallback {
  void onCommentsFetching();

  void onCommentsAvailable(ArrayList<CommentModel> comments);

  void onCommentsFetchError(String error);
}
