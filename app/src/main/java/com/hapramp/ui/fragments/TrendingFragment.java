package com.hapramp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.datastore.ServiceWorker;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
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

/**
 * Created by Ankit on 4/14/2018.
 */

public class TrendingFragment extends Fragment implements FeedListView.FeedListViewListener, ServiceWorkerCallback {

    @BindView(R.id.feedListView)
    FeedListView feedListView;
    Unbinder unbinder;
    private ServiceWorkerRequestBuilder serviceWorkerRequestParamsBuilder;
    private ServiceWorkerRequestParams serviceWorkerRequestParams;
    private ServiceWorker serviceWorker;
    private String lastAuthor;
    private String lastPermlink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareServiceWorker();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        feedListView.setFeedListViewListener(this);
        feedListView.initialLoading();
        feedListView.setTopMarginForShimmer(16);
        feedListView.setTopMarginForRecyclerView(16);
        fetchPosts();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void prepareServiceWorker() {

        serviceWorker = new ServiceWorker();
        serviceWorker.init(getActivity());
        serviceWorker.setServiceWorkerCallback(this);
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder()
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT);

    }

    private void fetchPosts() {

        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();

        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(Communities.TAG_HAPRAMP)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .createRequestParam();

        serviceWorker.requestTrendingPosts(serviceWorkerRequestParams);

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

        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();

        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder.serCommunityTag(Communities.TAG_HAPRAMP)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setLastAuthor(lastAuthor)
                .setLastPermlink(lastPermlink)
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .createRequestParam();

        serviceWorker.requestAppendableFeedForTrending(serviceWorkerRequestParams);

    }

    @Override
    public void onHideCommunityList() {
        //NA
    }

    @Override
    public void onShowCommunityList() {
        //NA
    }


    // SERVICE WORKER CALLBACKS

    @Override
    public void onLoadingFromCache() {
        //NA
    }

    @Override
    public void onCacheLoadFailed() {
        //NA
    }

    @Override
    public void onNoDataInCache() {
        //NA
    }

    @Override
    public void onLoadedFromCache(ArrayList<Feed> cachedList, String lastAuthor , String lastPermlink) {
        //NA
    }

    @Override
    public void onFetchingFromServer() {
        if (feedListView != null) {
            feedListView.feedRefreshing(false);
        }
    }

    @Override
    public void onFeedsFetched(ArrayList<Feed> body, String lastAuthor , String lastPermlink) {
        if (feedListView != null) {
            feedListView.feedsRefreshed(body);
        }
    }

    @Override
    public void onFetchingFromServerFailed() {
        if (feedListView != null) {
            feedListView.failedToRefresh("");
        }
    }

    @Override
    public void onNoDataAvailable() {

    }

    @Override
    public void onRefreshing() {
        if (feedListView != null) {
            feedListView.feedRefreshing(false);
        }
    }

    @Override
    public void onRefreshed(List<Feed> refreshedList, String lastAuthor , String lastPermlink) {
        if (feedListView != null) {
            feedListView.setHasMoreToLoad(refreshedList.size() == Constants.MAX_FEED_LOAD_LIMIT);
            feedListView.feedsRefreshed(refreshedList);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }
    }

    @Override
    public void onRefreshFailed() {
        if (feedListView != null) {
            feedListView.failedToRefresh("");
        }
    }

    @Override
    public void onLoadingAppendableData() {
        //NA
    }

    @Override
    public void onAppendableDataLoaded(List<Feed> appendableList, String lastAuthor , String lastPermlink) {

        if (feedListView != null) {
            feedListView.setHasMoreToLoad(appendableList.size() == Constants.MAX_FEED_LOAD_LIMIT);
            feedListView.loadedMoreFeeds(appendableList);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }

    }

    @Override
    public void onAppendableDataLoadingFailed() {
        //NA
    }
}
