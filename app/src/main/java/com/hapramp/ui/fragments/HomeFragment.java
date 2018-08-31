package com.hapramp.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RawApiCaller;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.response.UserModel;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.ShadowUtils;
import com.hapramp.views.CommunityFilterView;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements LikePostCallback, FeedListView.FeedListViewListener, RawApiCaller.FeedDataCallback, CommunityFilterView.CommunityFilterCallback {
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
  private RawApiCaller rawApiCaller;
  private String last_author;
  private String last_permlink;

  public HomeFragment() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "home fragment");
    rawApiCaller = new RawApiCaller(mContext);
    rawApiCaller.setDataCallback(this);
  }

  private void initCategoryView() {
    try {
      Drawable drawable = ShadowUtils.generateBackgroundWithShadow(communityFilterView, R.color.white, R.dimen.communitybar_shadow_radius, R.color.Black12, R.dimen.communitybar_shadow_elevation, Gravity.BOTTOM);
      communityFilterView.setBackground(drawable);
      CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      ArrayList<CommunityModel> communityModels = new ArrayList<>();
      communityModels.add(0, new CommunityModel("", Communities.IMAGE_URI_ALL, Communities.ALL, "", "Feed", 0));
      communityModels.addAll(cwr.getCommunityModels());
      communityFilterView.setCommunityFilterCallback(this);
      communityFilterView.addCommunities(communityModels);
    }
    catch (Exception e) {
      Log.e(TAG, e.toString());
      fetchUserCommunities();
    }
  }

  private void fetchUserCommunities() {
    showLoadingProgressBarWithMessage("Getting your communities...");
    RetrofitServiceGenerator.getService().fetchUserCommunities(mCurrentUser)
      .enqueue(new Callback<UserModel>() {
        @Override
        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
          hideLoadingProgressBar();
          if (response.isSuccessful()) {
            HaprampPreferenceManager.getInstance()
              .saveUserSelectedCommunitiesAsJson(new Gson()
                .toJson(new CommunityListWrapper(response.body().communityModels)));
            initCategoryView();
          } else {
            onFailedToFetchUserCommunities();
          }
        }

        @Override
        public void onFailure(Call<UserModel> call, Throwable t) {
          onFailedToFetchUserCommunities();
        }
      });
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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() > 0) {
      initCategoryView();
    } else {
      fetchUserCommunities();
    }
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    feedListView.setFeedListViewListener(this);
    feedListView.initialLoading();
    feedListView.setTopMarginForShimmer(104);
    fetchAllPosts();
  }

  @Override
  public void onRefreshFeeds() {
    if (currentSelectedTag.equals(Communities.ALL)) {
      fetchAllPosts();
    } else {
      fetchCommunityPosts(currentSelectedTag);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void onFailedToFetchUserCommunities() {
    showAlertDialog("Failed to load your commmunities, Please try again!",
      "Try Again", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          fetchUserCommunities();
        }
      }, "Cancel",
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          dialogInterface.dismiss();
        }
      });
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
      rawApiCaller.requestMoreUserFeed(HaprampPreferenceManager.getInstance()
        .getCurrentSteemUsername(), last_author, last_permlink);
    }
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds, boolean appendableData) {
    if (feedListView != null) {
      if (appendableData) {
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

  private void fetchAllPosts() {
    feedListView.feedRefreshing(false);
    rawApiCaller.requestUserFeed(HaprampPreferenceManager.getInstance()
      .getCurrentSteemUsername());
  }

  private void fetchCommunityPosts(String tag) {
    feedListView.feedRefreshing(false);
    tag = tag.replace("hapramp-", "");
    rawApiCaller.requestCuratedFeedsByTag(tag);
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

  private void showLoadingProgressBarWithMessage(String msg) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(mContext);
    }
    progressDialog.setMessage(msg);
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  private void hideLoadingProgressBar() {
    if (progressDialog != null) {
      progressDialog.hide();
    }
  }

  private void showAlertDialog(String msg, String positiveButtonText,
                               DialogInterface.OnClickListener positiveListener,
                               String negativeButtonText,
                               DialogInterface.OnClickListener negativeListener) {
    alertDialog = new AlertDialog.Builder(mContext)
      .setMessage(msg)
      .setPositiveButton(positiveButtonText, positiveListener)
      .setNegativeButton(negativeButtonText, negativeListener)
      .create();
    alertDialog.show();
  }

  @Override
  public void onDataLoadError() {
    if (feedListView != null) {
      feedListView.failedToRefresh("");
    }
  }

  @Override
  public void onCommunitySelected(String tag) {
    feedListView.initialLoading();
    currentSelectedTag = tag;
    if (tag.equals(Communities.ALL)) {
      fetchAllPosts();
    } else {
      fetchCommunityPosts(tag);
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_BROWSE_HOME);
  }
}
