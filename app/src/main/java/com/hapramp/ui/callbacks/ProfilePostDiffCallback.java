package com.hapramp.ui.callbacks;

import android.support.v7.util.DiffUtil;

import com.hapramp.steem.models.Feed;

import java.util.List;
import java.util.Objects;

/**
 * Created by Ankit on 5/10/2018.
 */

public class ProfilePostDiffCallback extends DiffUtil.Callback {

  private List<Feed> mOldFeedList;
  private List<Feed> mNewFeedList;

  public ProfilePostDiffCallback(List<Feed> oldList, List<Feed> newList) {
    this.mOldFeedList = oldList;
    this.mNewFeedList = newList;
  }

  @Override
  public int getOldListSize() {
    return mOldFeedList.size();
  }

  @Override
  public int getNewListSize() {
    return mNewFeedList.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return Objects.equals(mOldFeedList.get(oldItemPosition).id, mNewFeedList.get(newItemPosition).id);
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final Feed oldFeed = mOldFeedList.get(oldItemPosition);
    final Feed newFeed = mNewFeedList.get(newItemPosition);
    return Objects.equals(newFeed.totalPayoutValue, oldFeed.totalPayoutValue);
  }

}
