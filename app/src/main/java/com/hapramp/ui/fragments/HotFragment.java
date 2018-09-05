package com.hapramp.ui.fragments;

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
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.views.feedlist.FeedListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/14/2018.
 */

public class HotFragment extends Fragment implements FeedListView.FeedListViewListener{
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  private Unbinder unbinder;
  private Context context;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
    AnalyticsUtil.getInstance(getActivity()).setCurrentScreen(getActivity(), AnalyticsParams.SCREEN_HOT, null);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "hot fragment");
    setRetainInstance(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_hot, null);
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
   // rawApiCaller.requestHotFeeds(context);
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
}
