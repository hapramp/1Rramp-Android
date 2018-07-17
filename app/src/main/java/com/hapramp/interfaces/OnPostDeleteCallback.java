package com.hapramp.interfaces;

/**
 * Created by Ankit on 12/12/2017.
 */

public interface OnPostDeleteCallback {
  void onPostDeleted(int position);

  void onPostDeleteFailed();
}
