package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.MicroCommunityPostFilterView;
import com.hapramp.views.feedlist.FeedListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommunityPostFragment extends Fragment implements UserFeedCallback, FeedListView.FeedListViewListener {
  public static final String ORDER_TRENDING = "trending";
  public static final String ORDER_NEW = "created";
  public static final String ORDER_HOT = "hot";
  public static final String COMMUNITY_TAG = "community_tag";
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  @BindView(R.id.filterView)
  MicroCommunityPostFilterView filterView;
  Unbinder unbinder;
  private String mCurrentOrder = ORDER_TRENDING;
  private String mCommunityTag = "";
  private Context mContext;
  private DataStore dataStore;
  private String last_author;
  private String last_permlink;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.community_post_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    Bundle bundle = getArguments();
    mCommunityTag = bundle.getString(COMMUNITY_TAG);
    dataStore = new DataStore();
    setupPostList();
    setupFilter();
    fetchPosts();
    return view;
  }

  private void setupPostList() {
    feedListView.setFeedListViewListener(this);
    feedListView.initialLoading();
    feedListView.setMessageWhenNoData("Error",
      "Something went wrong!");
  }

  private void setupFilter() {
    filterView.setFilterItemSelectionCallback(new MicroCommunityPostFilterView.FilterItemSelectionCallback() {
      @Override
      public void onItemSelected(String order) {
        mCurrentOrder = order;
        fetchPosts();
      }
    });
  }

  private void fetchPosts() {
    feedListView.initialLoading();
    dataStore.requestMicroCommunityPosts(mCommunityTag, mCurrentOrder, false, this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onFeedsFetching() {

  }

  @Override
  public void onUserFeedsAvailable(List<Feed> feeds, boolean isFreshData, boolean isAppendable) {
    if (feedListView != null) {
      if (isAppendable) {
        if (feeds.size() > 0) {
          feeds.remove(0);
        }
        feedListView.loadedMoreFeeds(feeds);
      } else {
        feedListView.feedsRefreshed(feeds);
      }
      int size = feeds.size();
      if (feeds.size() > 0) {
        last_author = feeds.get(size - 1).getAuthor();
        last_permlink = feeds.get(size - 1).getPermlink();
      }
    }
  }

  @Override
  public void onUserFeedFetchError(String err) {
    if (feedListView != null) {
      feedListView.failedToRefresh("");
    }
  }

  @Override
  public void onRetryFeedLoading() {
    fetchPosts();
  }

  @Override
  public void onRefreshFeeds() {
    refreshPosts();
  }

  private void refreshPosts() {
    dataStore.requestMicroCommunityPosts(mCommunityTag, mCurrentOrder, true, this);
  }

  @Override
  public void onLoadMoreFeeds() {
    fetchMorePosts();
  }

  private void fetchMorePosts() {
    dataStore.requestMicroCommunityPosts(mCommunityTag, mCurrentOrder, last_author, last_permlink,
      this);
  }

  @Override
  public void onHideCommunityList() {

  }

  @Override
  public void onShowCommunityList() {

  }
}
