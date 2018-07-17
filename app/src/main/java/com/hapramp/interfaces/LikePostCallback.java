package com.hapramp.interfaces;

/**
 * Created by Ankit on 11/2/2017.
 */

public interface LikePostCallback {
  void onPostLiked(int postId);

  void onPostLikeError();
}
