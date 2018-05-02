package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.CachePreference;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.FeedResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceWorker {

    private static final String TAG = ServiceWorker.class.getSimpleName();
    ServiceWorkerCallback serviceWorkerCallback;
    private Handler mHandler;
    private CachePreference mCachePreference;
    private ServiceWorkerRequestParams currentRequestParams;

    // setup all the deps
    public void init(Context context) {
        l("init()");
        mCachePreference = CachePreference.getInstance();
        mHandler = new Handler();
    }

    public void setServiceWorkerCallback(ServiceWorkerCallback serviceWorkerCallback) {
        l("setting callback");
        this.serviceWorkerCallback = serviceWorkerCallback;
    }

    //method to get all feeds(cached or fresh)
    public void requestAllFeeds(final ServiceWorkerRequestParams requestParams) {

        l("Requesting All Feeds : " + requestParams.getCommunityTag());
        this.currentRequestParams = requestParams;
        if (mCachePreference.isFeedResponseCached()) {
            l("Feed was cached");
            // read from databse and return
            new Thread() {
                @Override
                public void run() {
                    final FeedResponse cachedFeedResponse = mCachePreference.getCachedFeedResponse();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (serviceWorkerCallback != null) {
                                l("Returning from cache");
                                serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cachedFeedResponse.getFeeds());
                            }
                        }
                    });
                }
            }.start();

            l("Fetching from server");
            // and start refreshing...
            fetchAllFeeds(requestParams);

        } else {
            l("Feed Was not cached");
            // no cache
            // start refreshing
            l("Fetching from server");
            fetchAllFeeds(requestParams);
        }

    }

    //method to get community feeds from cache or api server
    public void requestCommunityFeeds(final ServiceWorkerRequestParams requestParams) {

        l("Requesting Community Feeds " + requestParams.getCommunityTag());

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onLoadingFromCache();
        }

        this.currentRequestParams = requestParams;
        //check for cache
        if (mCachePreference.isFeedResponseCached()) {
            new Thread() {
                @Override
                public void run() {
                    final FeedResponse feedResponse = mCachePreference.getCachedFeedResponse();
                    //check for live request
                    if (isRequestLive(requestParams)) {
                        //call on main thread
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                List<Feed> filteredFeeds = FeedsFilter.filter(feedResponse.getFeeds(), currentRequestParams.getCommunityTag());
                                serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) filteredFeeds);
                            }
                        });
                    }
                }
            }.start();

            // start fetching from server
            fetchAllFeeds(requestParams);

        } else {

            // start fetching feeds
            fetchAllFeeds(requestParams);

        }

    }

    /*
     * Public method for fetching trailing feeds, these feeds are intermediate feeds and need not to be cached.
     * */
    public void requestAppendableFeed(ServiceWorkerRequestParams requestParams) {
        this.currentRequestParams = requestParams;

        // TODO: 2/28/2018
        // 1 - call api for feeds
        // onSucess: isRequestLive() --> onAppendableDataFetched()
        // onFailed: isRequestLive() --> onAppendableFetchFailed()

    }

    /*
     * Public method for requesting refresh of feeds.
     * Since the feeds are updated, caching is an obvious option here.
     * INPUT: take #communityId for type of feed to refresh
     * */
    public void requestRefreshFeeds(ServiceWorkerRequestParams requestParams) {

        this.currentRequestParams = requestParams;
        // TODO: 2/28/2018
        // 1 - call api for feeds
        // onSucess: isRequestLive() --> onRefreshed()
        // onFailed: isRequestLive() --> onRefreshFailed
        // cache the refreshed feeds

    }

    /*
     * Private method to make a fresh feeds call to api server.
     * This method generally request the feed and cache them.
     * INPUT: take #communityId for type of feed to refresh (in future)
     * */
    private void fetchAllFeeds(final ServiceWorkerRequestParams feedRequestParams) {

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServer();
        }

        // TODO: 2/28/2018
        // 1 - call api for feeds
        RetrofitServiceGenerator.getService()
                .getUserFeeds(feedRequestParams.getUsername(), feedRequestParams.getLimit())
                .enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {

                        //Profile.fetchUserProfilesFor(response.body());

                        // check for life of request(whether other request has came and over-written on this)
                        if (isRequestLive(feedRequestParams)) {

                            if (serviceWorkerCallback != null) {

                                if (response.isSuccessful()) {

                                    //cache the results after filtering them on basis of category
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            l("Caching Feeds!");
                                            mCachePreference.cacheFeedResponse(response.body());
                                        }
                                    }.start();

                                    // check for the request: is it for filtered feeds(community)
                                    if (!isRequestForCommunityFeed(feedRequestParams)) {

                                        l("Returning All Feeds");
                                        // return all feeds
                                        serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds());

                                    } else {
                                        // return community feeds
                                        // read on worker thread and return
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                l("Returning Community Feeds");
                                                mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        List<Feed> filteredFeeds = FeedsFilter.filter(response.body().getFeeds(),
                                                                currentRequestParams.getCommunityTag());

                                                        serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) filteredFeeds);

                                                    }
                                                });
                                            }
                                        }.start();
                                    }

                                } else {
                                    // report error
                                    l("Error[fetchAllFeeds_onResponse]");
                                    serviceWorkerCallback.onFetchingFromServerFailed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        l("Error[fetchAllFeeds_onFailure]" + t.toString());
                        if (serviceWorkerCallback != null) {
                            serviceWorkerCallback.onFetchingFromServerFailed();
                        }
                    }

                });

    }

    public void requestTrendingPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {

        this.currentRequestParams = serviceWorkerRequestParams;

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServer();
        }

        RetrofitServiceGenerator.getService().getTrendingFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
                .enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        //Profile.fetchUserProfilesFor(response.body());

                        // check for life of request(whether other request has came and over-written on this)
                        if (isRequestLive(serviceWorkerRequestParams)) {

                            if (serviceWorkerCallback != null) {

                                if (response.isSuccessful()) {
                                    l("Returning All Feeds");
                                    // return all feeds
                                    serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds());
                                } else {
                                    l("Error :(");
                                    serviceWorkerCallback.onFetchingFromServerFailed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        l("Error :(");
                        serviceWorkerCallback.onFetchingFromServerFailed();
                    }
                });

    }

    public void requestHotPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {

        this.currentRequestParams = serviceWorkerRequestParams;

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServer();
        }

        RetrofitServiceGenerator.getService().getHotFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
                .enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {

                        //Profile.fetchUserProfilesFor(response.body());
                        // check for life of request(whether other request has came and over-written on this)
                        if (isRequestLive(serviceWorkerRequestParams)) {

                            if (serviceWorkerCallback != null) {

                                if (response.isSuccessful()) {
                                    l("Returning All Feeds");
                                    // return all feeds
                                    serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds());
                                } else {
                                    l("Error :(");
                                    serviceWorkerCallback.onFetchingFromServerFailed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        l("Error :(");
                        serviceWorkerCallback.onFetchingFromServerFailed();
                    }
                });

    }

    public void requestLatestPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {

        this.currentRequestParams = serviceWorkerRequestParams;

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServer();
        }

        RetrofitServiceGenerator.getService().getLatestFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
                .enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        //Profile.fetchUserProfilesFor(response.body());

                        // check for life of request(whether other request has came and over-written on this)
                        if (isRequestLive(serviceWorkerRequestParams)) {

                            if (serviceWorkerCallback != null) {

                                if (response.isSuccessful()) {
                                    l("Returning All Feeds");
                                    // return all feeds
                                    serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds());
                                } else {
                                    l("Error :(");
                                    serviceWorkerCallback.onFetchingFromServerFailed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        l("Error :(");
                        serviceWorkerCallback.onFetchingFromServerFailed();
                    }
                });

    }

    /*
     * Method to check if the request is live or overridden by other request
     * */
    private boolean isRequestLive(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return serviceWorkerRequestParams.equals(currentRequestParams);
    }

    private boolean isRequestForCommunityFeed(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return !serviceWorkerRequestParams.getCommunityTag().equals(Communities.ALL);
    }

    /* Helper method for logging
     * */
    private void l(String msg) {
        Log.d(TAG, msg);
    }


}
