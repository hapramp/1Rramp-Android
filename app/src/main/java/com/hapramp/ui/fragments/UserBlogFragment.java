package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.feedlist.FeedListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserBlogFragment extends Fragment implements UserFeedCallback, FeedListView.FeedListViewListener {
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  Unbinder unbinder;
  private DataStore dataStore;
  private String mUsername;
  private String last_author;
  private String last_permlink;

  public UserBlogFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dataStore = new DataStore();
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_blog, container, false);
    unbinder = ButterKnife.bind(this, view);
    feedListView.setFeedListViewListener(this);
    feedListView.initialLoading();
    fetchAllPosts();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void fetchAllPosts() {
    dataStore.requestUserBlog(mUsername, false, this);
  }

  public void setUsername(String username) {
    this.mUsername = username;
  }

  @Override
  public void onFeedsFetching() {

  }

  @Override
  public void onUserFeedsAvailable(List<Feed> feeds, boolean isFreshData, boolean isAppendable) {
    injectResteemData(feeds);
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

  private void injectResteemData(List<Feed> feeds) {
    for (int i = 0; i < feeds.size(); i++) {
      if (!feeds.get(i).getAuthor().equals(mUsername)) {
        feeds.get(i).setResteemed(true);
      }
    }
  }

  @Override
  public void onRetryFeedLoading() {

  }

  @Override
  public void onRefreshFeeds() {
    refreshAllPosts();
  }

  @Override
  public void onLoadMoreFeeds() {
    if (last_author.length() > 0) {
      dataStore.requestUserBlog(mUsername, last_author, last_permlink, this);
    }
  }

  @Override
  public void onHideCommunityList() {

  }

  @Override
  public void onShowCommunityList() {

  }

  private void refreshAllPosts() {
    dataStore.requestUserBlog(mUsername, true, this);
  }

}
