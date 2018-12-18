package com.hapramp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.activity.CommunitySelectionActivity;
import com.hapramp.ui.activity.HomeActivity;
import com.hapramp.ui.activity.UserSearchActivity;
import com.hapramp.utils.CommunityIds;
import com.hapramp.utils.CommunitySortUtils;
import com.hapramp.utils.ShadowUtils;
import com.hapramp.views.CommunityFilterView;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hapramp.steem.Communities.EXPLORE;
import static com.hapramp.steem.Communities.FEEDS;

public class HomeFragment extends Fragment implements LikePostCallback, FeedListView.FeedListViewListener, CommunityFilterView.CommunityFilterCallback, UserFeedCallback {
  public static final String TAG = HomeFragment.class.getSimpleName();
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  @BindView(R.id.progressBarLoadingRecite)
  ProgressBar progressBarLoadingRecite;
  @BindView(R.id.communityFilterView)
  CommunityFilterView communityFilterView;
  private Context mContext;
  private String currentSelectedTag = EXPLORE;
  private Unbinder unbinder;
  private String mUsername;
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
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    fetchExplorePosts();
    if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() > 0) {
      initCategoryView();
    } else {
      navigateToCommunitySelectionPage();
    }
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    feedListView.setFeedListViewListener(this);
    feedListView.initialLoading();
    feedListView.setMessageWhenNoData("We don't want you to feel lonely",
      "Find what's new on 1Ramp on the search page");
    feedListView.setClickListenerOnErrorPanelMessage(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(mContext, UserSearchActivity.class);
        mContext.startActivity(i);
        ((HomeActivity) mContext).overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
      }
    });
    feedListView.setTopMarginForShimmer(140);
  }

  @Override
  public void onStart() {
    super.onStart();
    AnalyticsUtil.getInstance(mContext).setCurrentScreen((Activity) mContext,
      AnalyticsParams.SCREEN_HOME, null);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void fetchExplorePosts() {
    dataStore.requestExploreFeeds(this);
  }

  private void initCategoryView() {
    try {
      CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance()
        .getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      if (cwr.getCommunityModels().size() == 0) {
        navigateToCommunitySelectionPage();
        return;
      }
      ArrayList<CommunityModel> communityModels = new ArrayList<>();
      //add explore tab
      communityModels.add(0, new CommunityModel("", "",
        EXPLORE, "", "Explore", CommunityIds.EXPLORE));
      //add feed tab
      communityModels.add(1, new CommunityModel("", "",
        FEEDS, "", "Feed", CommunityIds.FEED));
      CommunitySortUtils.sortCommunity(cwr.getCommunityModels());
      communityModels.addAll(cwr.getCommunityModels());
      communityFilterView.setCommunityFilterCallback(this);
      communityFilterView.addCommunities(communityModels);
    }
    catch (Exception e) {
      Log.e(TAG, e.toString());
    }
  }

  private void navigateToCommunitySelectionPage() {
    Intent intent = new Intent(mContext, CommunitySelectionActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    mContext.startActivity(intent);
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
  public void onRefreshFeeds() {
    if (currentSelectedTag.equals(FEEDS)) {
      refreshAllPosts();
    } else if (currentSelectedTag.equals(EXPLORE)) {
      refreshExplorePosts();
    } else {
      refreshCommunityPosts(currentSelectedTag);
    }
  }

  @Override
  public void onLoadMoreFeeds() {
    if (currentSelectedTag.equals(FEEDS)) {
      if (last_author.length() > 0) {
        dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
          .getCurrentSteemUsername(), last_author, last_permlink, this);
      }
    } else if (currentSelectedTag.equals(EXPLORE)) {
      // TODO: 16/10/18 no pagination available for now.
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          onUserFeedsAvailable(new ArrayList<Feed>(), false, true);
        }
      }, 2000);
    }
  }

  private void injectResteemData(List<Feed> feeds) {
    Set<String> followings = HaprampPreferenceManager.getInstance().getFollowingsSet();
    if (followings != null) {
      for (int i = 0; i < feeds.size(); i++) {
        if (!followings.contains(feeds.get(i).getAuthor())) {
          feeds.get(i).setResteemed(true);
        }
      }
    }
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

  private void refreshAllPosts() {
    dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), true, this);
  }

  private void refreshExplorePosts() {
    dataStore.requestExploreFeeds(true,this);
  }

  private void refreshCommunityPosts(String tag) {
    tag = tag.replace("hapramp-", "");
    dataStore.requestCommunityFeed(tag, true, this);
  }

  private void fetchCommunityPosts(String tag) {
    tag = tag.replace("hapramp-", "");
    dataStore.requestCommunityFeed(tag, false, this);
  }

  @Override
  public void onCommunitySelected(String tag) {
    feedListView.initialLoading();
    currentSelectedTag = tag;
    last_permlink = "";
    last_author = "";
    if (tag.equals(FEEDS)) {
      fetchAllPosts();
    } else if (tag.equals(EXPLORE)) {
      fetchExplorePosts();
    } else {
      fetchCommunityPosts(tag);
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_BROWSE_HOME);
  }

  private void fetchAllPosts() {
    dataStore.requestUserFeed(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername(), false, this);
  }

  @Override
  public void onFeedsFetching() {
  }

  @Override
  public void onUserFeedsAvailable(List<Feed> feeds, boolean isFreshData, boolean isAppendable) {
    if (currentSelectedTag.equals(FEEDS)) {
      injectResteemData(feeds);
    }
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
