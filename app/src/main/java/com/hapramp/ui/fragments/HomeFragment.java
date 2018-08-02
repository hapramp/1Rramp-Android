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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.datamodels.response.UserModel;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.ServiceWorkerRequestBuilder;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.CategoryRecyclerAdapter;
import com.hapramp.utils.Constants;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.ShadowUtils;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements
  CategoryRecyclerAdapter.OnCategoryItemClickListener, LikePostCallback, FeedListView.FeedListViewListener, ServiceWorkerCallback, RawApiCaller.DataCallback {
  public static final String ALL = "all";
  public static final String TAG = HomeFragment.class.getSimpleName();
  @BindView(R.id.feedListView)
  FeedListView feedListView;
  @BindView(R.id.sectionsRv)
  RecyclerView sectionsRv;
  @BindView(R.id.progressBarLoadingRecite)
  ProgressBar progressBarLoadingRecite;
  //ServiceWorker serviceWorker;
  private Context mContext;
  private String currentSelectedTag = ALL;
  private CategoryRecyclerAdapter categoryRecyclerAdapter;
  private Unbinder unbinder;
  private ServiceWorkerRequestParams serviceWorkerRequestParams;
  private ServiceWorkerRequestBuilder serviceWorkerRequestParamsBuilder;
  private String lastAuthor;
  private String lastPermlink;
  private String mCurrentUser;
  private ProgressDialog progressDialog;
  private AlertDialog alertDialog;
  private Handler mHandler;
  private RawApiCaller rawApiCaller;

  public HomeFragment() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "home fragment");
    mHandler = new Handler();
    rawApiCaller = new RawApiCaller();
  }

<<<<<<< HEAD
  private void loadTestData(String tag) {
    rawApiCaller.requestNewFeeds(mContext,tag);
=======
  private void loadTestData() {
    rawApiCaller.requestNewFeeds(mContext);
>>>>>>> ec40afb9a4ebe812020d637080638571a4bbfc45
    rawApiCaller.setDataCallback(this);
  }
  private void initCategoryView() {
    try {
      categoryRecyclerAdapter = new CategoryRecyclerAdapter(mContext, this);
      sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
      sectionsRv.setAdapter(categoryRecyclerAdapter);
      Drawable drawable = ShadowUtils.generateBackgroundWithShadow(sectionsRv, R.color.white, R.dimen.communitybar_shadow_radius, R.color.Black12, R.dimen.communitybar_shadow_elevation, Gravity.BOTTOM);
      sectionsRv.setBackground(drawable);
      CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      ArrayList<CommunityModel> communityModels = new ArrayList<>();
      communityModels.add(0, new CommunityModel("", Communities.IMAGE_URI_ALL, Communities.ALL, "", "All", 0));
      communityModels.addAll(cwr.getCommunityModels());
      categoryRecyclerAdapter.setCommunities(communityModels);
    }
    catch (Exception e) {
      Log.e(TAG, e.toString());
      fetchUserCommunities();
    }
  }

  @Override
  public void onCategoryClicked(String tag) {
    feedListView.initialLoading();
    currentSelectedTag = tag;
    if (tag.equals(Communities.ALL)) {
      fetchAllPosts();
    } else {
      fetchCommunityPosts(tag);
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_BROWSE_HOME);
  }

  private void fetchCommunityPosts(String tag) {
    serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
    serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(tag)
      .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
      .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
      .createRequestParam();
    // serviceWorker.requestCommunityFeeds(serviceWorkerRequestParams);
<<<<<<< HEAD
    loadTestData(tag);
=======
    loadTestData();
>>>>>>> ec40afb9a4ebe812020d637080638571a4bbfc45
  }

  private void fetchUserCommunities() {
    showLoadingProgressBarWithMessage("Getting your communities...");
    RetrofitServiceGenerator.getService().fetchUserCommunities(mCurrentUser).enqueue(new Callback<UserModel>() {
      @Override
      public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        hideLoadingProgressBar();
        if (response.isSuccessful()) {
          HaprampPreferenceManager.getInstance().saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(response.body().communityModels)));
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
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mCurrentUser = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    prepareServiceWorker();
  }

  private void prepareServiceWorker() {
//    serviceWorker = new ServiceWorker();
//    serviceWorker.init(getActivity());
//    serviceWorker.setServiceWorkerCallback(this);
    serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder()
      .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
      .setLimit(100);
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
<<<<<<< HEAD
    //fetchAllPosts();
    loadTestData("bitcoin");
=======
    fetchAllPosts();
    loadTestData();
>>>>>>> ec40afb9a4ebe812020d637080638571a4bbfc45
  }

  @Override
  public void onStart() {
    super.onStart();
    AnalyticsUtil.getInstance(mContext).setCurrentScreen((Activity) mContext, AnalyticsParams.SCREEN_HOME, null);
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
  public void onRefreshFeeds() {
    fetchCommunityPosts(currentSelectedTag);
  }

  @Override
  public void onLoadMoreFeeds() {
    loadMore(currentSelectedTag);
  }

  //  CALLBACKS FROM FEED LIST VIEW

  private void fetchAllPosts() {
    serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
    serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(Communities.ALL)
      .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
      .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
      .createRequestParam();
    //serviceWorker.requestAllFeeds(serviceWorkerRequestParams);
<<<<<<< HEAD
=======
    loadTestData();
>>>>>>> ec40afb9a4ebe812020d637080638571a4bbfc45
  }

  @Override
  public void onHideCommunityList() {
    hideCategorySection();
  }

  private void hideCategorySection() {
    sectionsRv.animate().translationY(-sectionsRv.getMeasuredHeight());
    progressBarLoadingRecite.animate().translationY(-sectionsRv.getMeasuredHeight());
  }

  @Override
  public void onShowCommunityList() {
    bringBackCategorySection();
  }

  private void bringBackCategorySection() {
    sectionsRv.animate().translationY(0);
    progressBarLoadingRecite.animate().translationY(0);
  }


  //CALLBACKS FROM SERVICE WORKER

  @Override
  public void onLoadingFromCache() {

  }

  @Override
  public void onCacheLoadFailed() {
    if (feedListView != null) {
      feedListView.noCachedFeeds();
    }
  }

  @Override
  public void onNoDataInCache() {
    if (feedListView != null) {
      feedListView.noCachedFeeds();
    }
  }

  @Override
  public void onLoadedFromCache(ArrayList<Feed> cachedList, String lastAuthor, String lastPermlink) {
    if (feedListView != null) {
      if (currentSelectedTag.equals(Communities.ALL)) {
        feedListView.setHasMoreToLoad(cachedList.size() == Constants.MAX_FEED_LOAD_LIMIT);
      } else {
        feedListView.setHasMoreToLoad(cachedList.size() > 0);
      }

      feedListView.cachedFeedFetched(cachedList);
      this.lastAuthor = lastAuthor;
      this.lastPermlink = lastPermlink;
    }
  }

  @Override
  public void onFetchingFromServer() {
    if (feedListView != null) {
      feedListView.feedRefreshing(false);
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onFeedsFetched(ArrayList<Feed> feeds, String lastAuthor, String lastPermlink) {
    if (feedListView != null) {
      if (currentSelectedTag.equals(Communities.ALL)) {
        feedListView.setHasMoreToLoad(feeds.size() == Constants.MAX_FEED_LOAD_LIMIT);
      } else {
        feedListView.setHasMoreToLoad(feeds.size() > 0);
      }
      feedListView.feedsRefreshed(feeds);
      this.lastAuthor = lastAuthor;
      this.lastPermlink = lastPermlink;
    }

    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
  }

  @Override
  public void onFetchingFromServerFailed() {
    if (feedListView != null) {
      feedListView.failedToRefresh("");
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
  }

  @Override
  public void onNoDataAvailable() {
    if (feedListView != null) {
      feedListView.onNoDataAvailable();
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
  }

  @Override
  public void onRefreshing() {
    if (feedListView != null) {
      feedListView.feedRefreshing(false);
    }
  }

  @Override
  public void onRefreshed(List<Feed> refreshedList, String lastAuthor, String lastPermlink) {
    if (feedListView != null) {

      if (currentSelectedTag.equals(Communities.ALL)) {
        feedListView.setHasMoreToLoad(refreshedList.size() == Constants.MAX_FEED_LOAD_LIMIT);
      } else {
        feedListView.setHasMoreToLoad(refreshedList.size() > 0);
      }

      feedListView.feedsRefreshed(refreshedList);

      this.lastAuthor = lastAuthor;
      this.lastPermlink = lastPermlink;
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
  }

  @Override
  public void onRefreshFailed() {
    if (feedListView != null) {
      feedListView.failedToRefresh("");
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
  }

  @Override
  public void onLoadingAppendableData() {

  }

  @Override
  public void onAppendableDataLoaded(List<Feed> appendableList, String lastAuthor, String lastPermlink) {

    if (feedListView != null) {

      if (currentSelectedTag.equals(Communities.ALL)) {
        feedListView.setHasMoreToLoad(appendableList.size() == Constants.MAX_FEED_LOAD_LIMIT);
      } else {
        feedListView.setHasMoreToLoad(appendableList.size() > 0);
      }

      feedListView.loadedMoreFeeds(appendableList);
      this.lastAuthor = lastAuthor;
      this.lastPermlink = lastPermlink;
    }

    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }

  }

  @Override
  public void onAppendableDataLoadingFailed() {

    if (feedListView != null) {
      feedListView.failedToFetchAppendable();
    }
    if (progressBarLoadingRecite != null) {
      progressBarLoadingRecite.setVisibility(View.GONE);
    }
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

  private void showAlertDialog(String msg, String positiveButtonText, DialogInterface.OnClickListener positiveListener, String negativeButtonText, DialogInterface.OnClickListener negativeListener) {
    alertDialog = new AlertDialog.Builder(mContext)
      .setMessage(msg)
      .setPositiveButton(positiveButtonText, positiveListener)
      .setNegativeButton(negativeButtonText, negativeListener)
      .create();
    alertDialog.show();
  }

  private void loadMore(String tag) {
    serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
    serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(tag)
      .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
      .setLastAuthor(lastAuthor)
      .setLastPermlink(lastPermlink)
      .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
      .createRequestParam();
    // serviceWorker.requestAppendableFeed(serviceWorkerRequestParams);
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds) {
<<<<<<< HEAD
    if(feedListView!=null)
      feedListView.feedsRefreshed(feeds);
=======
    feedListView.feedsRefreshed(feeds);
>>>>>>> ec40afb9a4ebe812020d637080638571a4bbfc45
  }
}
