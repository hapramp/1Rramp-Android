package com.hapramp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RawApiCaller;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.ProfileRecyclerAdapter;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements RawApiCaller.FeedDataCallback, ProfileRecyclerAdapter.OnLoadMoreListener {
  private static final String TAG = ProfileFragment.class.getSimpleName();
  @BindView(R.id.profilePostRv)
  RecyclerView profilePostRv;
  @BindView(R.id.profileRefreshLayout)
  SwipeRefreshLayout profileRefreshLayout;
  private Context mContext;
  private Resources resources;
  private ProfileRecyclerAdapter profilePostAdapter;
  private ViewItemDecoration viewItemDecoration;
  private Unbinder unbinder;
  private LinearLayoutManager llm;
  private RawApiCaller rawApiCaller;
  private String username;
  private String last_author;
  private String last_permlink;


  public ProfileFragment() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "profile fragment");
    rawApiCaller = new RawApiCaller(mContext);
    rawApiCaller.setDataCallback(this);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    resources = mContext.getResources();
    AnalyticsUtil.getInstance(getActivity()).setCurrentScreen((Activity) mContext, AnalyticsParams.SCREEN_SELF_PROFILE, null);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
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
    profilePostAdapter = new ProfileRecyclerAdapter(mContext, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
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

  private void fetchPosts() {
    rawApiCaller.requestUserBlogs(username);
  }

  private void setScrollListener() {
    profileRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fetchPosts();
      }
    });
  }

  public void reloadPosts() {
    fetchPosts();
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds,boolean appendable) {
    if (profilePostRv != null) {
      if (appendable) {
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
  public void onDataLoadError() {
    if (profileRefreshLayout != null) {
      if (profileRefreshLayout.isRefreshing()) {
        profileRefreshLayout.setRefreshing(false);
      }
    }
  }

  @Override
  public void onLoadMore() {
    fetchMorePosts();
  }

  private void fetchMorePosts() {
    rawApiCaller.requestMoreUserBlogs(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), last_author, last_permlink);
  }
}
