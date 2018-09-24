package com.hapramp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserFeedCallback;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.activity.CommunitySelectionActivity;
import com.hapramp.utils.ShadowUtils;
import com.hapramp.views.CommunityFilterView;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements LikePostCallback, FeedListView.FeedListViewListener, CommunityFilterView.CommunityFilterCallback, UserFeedCallback {
  public static final String ALL = "all";
  public static final String TAG = HomeFragment.class.getSimpleName();
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  @BindView(R.id.progressBarLoadingRecite)
  ProgressBar progressBarLoadingRecite;
  @BindView(R.id.communityFilterView)
  CommunityFilterView communityFilterView;
  private Context mContext;
  private String currentSelectedTag = ALL;
  private Unbinder unbinder;
  private String mCurrentUser;
  private ProgressDialog progressDialog;
  private AlertDialog alertDialog;
  private DataStore dataStore;
  private String last_author;
  private String last_permlink;

  public HomeFragment() {
    EventReporter.addEvent(AnalyticsParams.EVENT_BROWSE_HOME);
    dataStore = new DataStore();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    fetchAllPosts();
    if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() > 0) {
      initCategoryView();
    } else {
      navigateToCommunitySelectionPage();
    }
    return view;
  }

  private void initCategoryView() {
    try {
      Drawable drawable = ShadowUtils.generateBackgroundWithShadow(communityFilterView,
        R.color.white, R.dimen.communitybar_shadow_radius, R.color.Black12, R.dimen.communitybar_shadow_elevation, Gravity.BOTTOM);
      communityFilterView.setBackground(drawable);
      CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance()
        .getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      if (cwr.getCommunityModels().size() == 0) {
        navigateToCommunitySelectionPage();
        return;
      }
      ArrayList<CommunityModel> communityModels = new ArrayList<>();
      communityModels.add(0, new CommunityModel("", Communities.IMAGE_URI_ALL,
        Communities.ALL, "", "Feed", 0));
      communityModels.addAll(cwr.getCommunityModels());
      communityFilterView.setCommunityFilterCallback(this);
      communityFilterView.addCommunities(communityModels);
    }
    catch (Exception e) {
      Log.e(TAG, e.toString());
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    AnalyticsUtil.getInstance(mContext).setCurrentScreen((Activity) mContext,
      AnalyticsParams.SCREEN_HOME, null);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mCurrentUser = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  private void navigateToCommunitySelectionPage() {
    Intent intent = new Intent(mContext, CommunitySelectionActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    mContext.startActivity(intent);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    feedListView.setFeedListViewListener(this);
    feedListView.initialLoading();
    feedListView.setTopMarginForShimmer(104);
  }

  @Override
  public void onRefreshFeeds() {
    if (currentSelectedTag.equals(Communities.ALL)) {
      refreshAllPosts();
    } else {
      refreshCommunityPosts(currentSelectedTag);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onPostLiked(int postId) {
  }

  @Override
  public void onPostLikeError() {
  }

  @Override
  public void onRetryFeedLoading() {
    Toast.makeText(mContext, "Retrying loading...", Toast.LENGTH_LONG).show();
    fetchCommunityPosts(currentSelectedTag);
  }

  @Override
  public void onLoadMoreFeeds() {
    if (currentSelectedTag.equals(ALL)) {
      if (last_author.length() > 0) {
        dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
          .getCurrentSteemUsername(), last_author, last_permlink, this);
      }
    }
  }

  private void refreshAllPosts() {
    dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), true, this);
  }

  private void refreshCommunityPosts(String tag) {
    tag = tag.replace("hapramp-", "");
    dataStore.requestCommunityFeed(tag, true, this);
  }

  @Override
  public void onCommunitySelected(String tag) {
    feedListView.initialLoading();
    currentSelectedTag = tag;
    last_permlink = "";
    last_author = "";
    if (tag.equals(Communities.ALL)) {
      fetchAllPosts();
    } else {
      fetchCommunityPosts(tag);
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_BROWSE_HOME);
  }

  private void fetchAllPosts() {
    dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), false, this);
  }

  private void fetchCommunityPosts(String tag) {
    tag = tag.replace("hapramp-", "");
    dataStore.requestCommunityFeed(tag, false, this);
  }

  @Override
  public void onHideCommunityList() {
    hideCategorySection();
  }

  private void hideCategorySection() {
    communityFilterView.animate().translationY(-communityFilterView.getMeasuredHeight());
    progressBarLoadingRecite.animate().translationY(-communityFilterView.getMeasuredHeight());
  }

  @Override
  public void onShowCommunityList() {
    bringBackCategorySection();
  }

  private void bringBackCategorySection() {
    communityFilterView.animate().translationY(0);
    progressBarLoadingRecite.animate().translationY(0);
  }

  @Override
  public void onFeedsFetching() {
  }

  @Override
  public void onUserFeedsAvailable(List<Feed> feeds, boolean isFreshData, boolean isAppendable) {
    if (feedListView != null) {
      if (isAppendable) {
        feeds.remove(0);
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
