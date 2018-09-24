package com.hapramp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.ProfileRecyclerAdapter;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements ProfileRecyclerAdapter.OnLoadMoreListener, UserFeedCallback {
  private static final String TAG = ProfileFragment.class.getSimpleName();
  @BindView(R.id.profilePostRv)
  RecyclerView profilePostRv;
  @BindView(R.id.profileRefreshLayout)
  SwipeRefreshLayout profileRefreshLayout;
  private Context mContext;
  private ProfileRecyclerAdapter profilePostAdapter;
  private ViewItemDecoration viewItemDecoration;
  private Unbinder unbinder;
  private LinearLayoutManager llm;
  private DataStore dataStore;
  private String username;
  private String last_author;
  private String last_permlink;


  public ProfileFragment() {
    EventReporter.addEvent(AnalyticsParams.EVENT_BROWSE_HOME);
    dataStore = new DataStore();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    AnalyticsUtil.getInstance(getActivity()).setCurrentScreen((Activity) mContext,
      AnalyticsParams.SCREEN_SELF_PROFILE, null);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    EventReporter.addEvent(AnalyticsParams.EVENT_PROFILE_SELF);
    username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    unbinder = ButterKnife.bind(this, view);
    init();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void init() {
    profilePostAdapter = new ProfileRecyclerAdapter(mContext,
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    profilePostAdapter.setOnLoadMoreListener(this);
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
    viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    profilePostRv.addItemDecoration(viewItemDecoration);
    llm = new LinearLayoutManager(mContext);
    profilePostRv.setLayoutManager(llm);
    profilePostRv.setAdapter(profilePostAdapter);
    setScrollListener();
    fetchPosts();
  }

  private void setScrollListener() {
    profileRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        refreshPosts();
      }
    });
  }

  private void fetchPosts() {
    dataStore.requestUserBlog(username, false, this);
  }

  private void refreshPosts() {
    dataStore.requestUserBlog(username, true, this);
  }

  public void reloadPosts() {
    refreshPosts();
  }

  @Override
  public void onLoadMore() {
    fetchMorePosts();
  }

  private void fetchMorePosts() {
    dataStore.requestUserBlog(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), last_author, last_permlink, this);
  }

  @Override
  public void onFeedsFetching() {

  }

  @Override
  public void onUserFeedsAvailable(List<Feed> feeds, boolean isFreshData, boolean isAppendable) {
    if (profilePostRv != null) {
      if (isAppendable) {
        feeds.remove(0);
        profilePostAdapter.appendPosts(feeds);
      } else {
        profilePostAdapter.setPosts(feeds);
        if (profileRefreshLayout != null) {
          if (profileRefreshLayout.isRefreshing()) {
            profileRefreshLayout.setRefreshing(false);
          }
        }
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
    if (profileRefreshLayout != null) {
      if (profileRefreshLayout.isRefreshing()) {
        profileRefreshLayout.setRefreshing(false);
      }
    }
  }
}
