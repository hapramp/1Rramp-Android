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
import com.hapramp.api.RawApiCaller;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/14/2018.
 */

public class LatestFragment extends Fragment implements FeedListView.FeedListViewListener, RawApiCaller.FeedDataCallback {
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  private Unbinder unbinder;
  private RawApiCaller rawApiCaller;
  private Context mContext;
  private String TAG = "hapramp";

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
    rawApiCaller = new RawApiCaller(mContext);
    rawApiCaller.setDataCallback(this);
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
    feedListView.initialLoading();
    fetchPosts();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void fetchPosts() {
    rawApiCaller.requestLatestPostsByTag(TAG);
  }

  //FEEDLIST CALLBACKS
  @Override
  public void onRetryFeedLoading() {
    fetchPosts();
  }

  @Override
  public void onRefreshFeeds() {
    fetchPosts();
  }

  @Override
  public void onLoadMoreFeeds() {
  }

  @Override
  public void onHideCommunityList() {
    //NA
  }

  @Override
  public void onShowCommunityList() {
    //NA
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds,boolean appendable) {
    if (feedListView != null) {
      feedListView.feedsRefreshed(feeds);
    }
  }

  @Override
  public void onDataLoadError() {
    if (feedListView != null) {
      feedListView.failedToRefresh("");
    }
  }
}
