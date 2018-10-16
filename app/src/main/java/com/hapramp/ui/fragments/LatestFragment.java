package com.hapramp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.views.feedlist.FeedListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/14/2018.
 */

public class LatestFragment extends Fragment implements FeedListView.FeedListViewListener, UserFeedCallback {
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  private Unbinder unbinder;
  private DataStore dataStore;
  private Context mContext;
  private String TAG = "hapramp";
  private String last_author;
  private String last_permlink;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    AnalyticsUtil.getInstance(getActivity()).setCurrentScreen((Activity) context, AnalyticsParams.SCREEN_NEW, null);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "latest fragment");
    dataStore = new DataStore();
    setRetainInstance(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_new, null);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    feedListView.setFeedListViewListener(this);
    fetchPosts();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void fetchPosts() {
    dataStore.requestPostsNewOn1Ramp(TAG, false, this);
  }

  //FEEDLIST CALLBACKS
  @Override
  public void onRetryFeedLoading() {
    fetchPosts();
  }

  @Override
  public void onRefreshFeeds() {
    refreshPosts();
  }

  @Override
  public void onLoadMoreFeeds() {
    fetchMorePosts();
  }

  private void fetchMorePosts() {
    dataStore.requestPostsNewOn1Ramp(TAG, last_author, last_permlink, this);
  }

  @Override
  public void onHideCommunityList() {
    //NA
  }

  @Override
  public void onShowCommunityList() {
    //NA
  }

  private void refreshPosts() {
    dataStore.requestPostsNewOn1Ramp(TAG, true, this);
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
}
