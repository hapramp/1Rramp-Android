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
import android.util.TypedValue;
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

public class ProfileFragment extends Fragment implements RawApiCaller.FeedDataCallback {
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
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    unbinder = ButterKnife.bind(this, view);
    init();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void init() {
    profilePostAdapter = new ProfileRecyclerAdapter(mContext, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
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

    profilePostRv.addOnScrollListener(new EndlessOnScrollListener(llm) {
      @Override
      public void onScrolledToEnd() {

      }
    });
  }

  public void reloadPosts() {
    fetchPosts();
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds,boolean appendable) {
    profilePostAdapter.setPosts(feeds);
    if (profileRefreshLayout != null) {
      if (profileRefreshLayout.isRefreshing()) {
        profileRefreshLayout.setRefreshing(false);
        Toast.makeText(mContext, "Refreshed your posts.", Toast.LENGTH_LONG).show();
      }
    }
  }

  @Override
  public void onDataLoadError() {
    if (profileRefreshLayout != null) {
      if (profileRefreshLayout.isRefreshing()) {
        profileRefreshLayout.setRefreshing(false);
        Toast.makeText(mContext, "Failed to refresh your posts.", Toast.LENGTH_LONG).show();
      }
    }
  }

  public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    // use your LayoutManager instead
    private LinearLayoutManager lm;

    EndlessOnScrollListener(LinearLayoutManager llm) {
      this.lm = llm;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      if (!recyclerView.canScrollVertically(1)) {
        onScrolledToEnd();
      }
    }

    public abstract void onScrolledToEnd();
  }
}
