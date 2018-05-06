package com.hapramp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.ui.adapters.CategoryRecyclerAdapter;
import com.hapramp.datastore.ServiceWorker;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.ServiceWorkerRequestBuilder;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements
        CategoryRecyclerAdapter.OnCategoryItemClickListener, LikePostCallback, FeedListView.FeedListViewListener, ServiceWorkerCallback {

    @BindView(R.id.feedListView)
    FeedListView feedListView;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;
    @BindView(R.id.progressBarLoadingRecite)
    ProgressBar progressBarLoadingRecite;

    private Context mContext;
    private String currentSelectedTag = ALL;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    ServiceWorker serviceWorker;
    private Unbinder unbinder;
    private ServiceWorkerRequestParams serviceWorkerRequestParams;
    public static final String ALL = "all";
    private ServiceWorkerRequestBuilder serviceWorkerRequestParamsBuilder;
    private String lastAuthor;
    private String lastPermlink;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareServiceWorker();
        //  serviceWorker.requestFeeds(serviceWorkerRequestParams);

    }

    private void prepareServiceWorker() {

        serviceWorker = new ServiceWorker();
        serviceWorker.init(getActivity());
        serviceWorker.setServiceWorkerCallback(this);
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder()
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .setLimit(100);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initCategoryView();
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

    private void bringBackCategorySection() {
        sectionsRv.animate().translationY(0);
        progressBarLoadingRecite.animate().translationY(0);
    }

    private void hideCategorySection() {
        sectionsRv.animate().translationY(-sectionsRv.getMeasuredHeight());
        progressBarLoadingRecite.animate().translationY(-sectionsRv.getMeasuredHeight());
    }

    private void initCategoryView() {

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(categoryRecyclerAdapter);

        CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
        ArrayList<CommunityModel> communityModels = new ArrayList<>();
        communityModels.add(0, new CommunityModel("", Communities.IMAGE_URI_ALL, Communities.ALL, "", "All", 0));
        communityModels.addAll(cwr.getCommunityModels());
        categoryRecyclerAdapter.setCommunities(communityModels);

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

    }

    private void fetchAllPosts() {

        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();

        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(Communities.ALL)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .createRequestParam();

        serviceWorker.requestAllFeeds(serviceWorkerRequestParams);

    }

    private void fetchCommunityPosts(String tag) {

        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();

        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(tag)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .createRequestParam();

        serviceWorker.requestCommunityFeeds(serviceWorkerRequestParams);

    }

    private void loadMore(String tag) {

        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();

        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(tag)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setLastAuthor(lastAuthor)
                .setLastPermlink(lastPermlink)
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .createRequestParam();

        serviceWorker.requestAppendableFeed(serviceWorkerRequestParams);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
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

    //  CALLBACKS FROM FEED LIST VIEW

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

    @Override
    public void onHideCommunityList() {
        hideCategorySection();
    }

    @Override
    public void onShowCommunityList() {
        bringBackCategorySection();
    }


    //CALLBACKS FROM SERVICE WORKER

    @Override
    public void onFetchingFromServer() {
        if (feedListView != null) {
            feedListView.feedRefreshing(false);
        }
        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefreshing() {
        if (feedListView != null) {
            feedListView.feedRefreshing(false);
        }
    }

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
        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefreshFailed() {
        if (feedListView != null) {
            feedListView.failedToRefresh("");
        }
        if(progressBarLoadingRecite!=null){
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

        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAppendableDataLoadingFailed() {

        if (feedListView != null) {
            feedListView.failedToFetchAppendable();
        }
        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
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

        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFetchingFromServerFailed() {
        if (feedListView != null) {
            feedListView.failedToRefresh("");
        }
        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoDataAvailable() {
        if (feedListView != null) {
            feedListView.onNoDataAvailable();
        }
        if(progressBarLoadingRecite!=null){
            progressBarLoadingRecite.setVisibility(View.GONE);
        }
    }

}
