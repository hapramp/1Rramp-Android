package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.CachePreference;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceWorker {

    private static final String TAG = ServiceWorker.class.getSimpleName();
    ServiceWorkerCallback serviceWorkerCallback;
    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private Handler mHandler;
    private CachePreference mCachePreference;
    private ServiceWorker serviceWorkerInstance;

    private ServiceWorkerRequestParams currentRequestParams;

    // setup all the deps
    public void init(Context context) {
        l("init()");
        this.mContext = context;
        mCachePreference = CachePreference.getInstance();
        mDatabaseHelper = new DatabaseHelper(context);
        mHandler = new Handler();

    }

    public void setServiceWorkerCallback(ServiceWorkerCallback serviceWorkerCallback) {
        l("setting callback");
        this.serviceWorkerCallback = serviceWorkerCallback;
    }

    //method to get feeds(cached or fresh)
    public void requestFeeds(final ServiceWorkerRequestParams requestParams) {
        l("requestFeed()");
        this.currentRequestParams = requestParams;
        // TODO: 2/28/2018
        // 1 - check for the cache, if found return else report its absence
        // TODO: 3/13/2018 cache should be update when data is cached
        if (mCachePreference.wasFeedCached()) {
            l("Feed was cached");
            // read from databse and return
            new Thread() {
                @Override
                public void run() {
                    final ArrayList<Feed> steemFeedModelArrayList = mDatabaseHelper.getFeed(requestParams.getCommunityId());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (serviceWorkerCallback != null) {
                                l("loaded from cache");
                                serviceWorkerCallback.onLoadedFromCache(steemFeedModelArrayList);
                            }
                        }
                    });
                }
            }.start();

            l("Fetching from server");
            // and start refreshing...
            fetchFeeds(requestParams);

        } else {
            l("Feed Was not cached");
            // no cache
            // start refreshing
            l("Fetching from server");
            fetchFeeds(requestParams);
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
    private void fetchFeeds(final ServiceWorkerRequestParams feedRequestParams) {

        if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServer();
        }

        // TODO: 2/28/2018
        // 1 - call api for feeds
        RetrofitServiceGenerator.getService().getUserFeeds(feedRequestParams.getUsername(), feedRequestParams.getLimit()).enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {

                if (isRequestLive(feedRequestParams)) {
                    if (serviceWorkerCallback != null) {
                        if (response.isSuccessful()) {
                            l("Feed Response :"+response.body().toString());
                            serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body());
                            //cache it
                            mDatabaseHelper.insertFeed((ArrayList<Feed>) response.body(), feedRequestParams.getCommunityId());
                            CachePreference.getInstance().setFeedCached(true);

                        } else {
                            // report error
                            l("Error :(");
                            serviceWorkerCallback.onFetchingFromServerFailed();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                l("Error : "+t.toString());
                if (serviceWorkerCallback != null) {
                    serviceWorkerCallback.onFetchingFromServerFailed();
                }
            }
        });

    }

    /*
     * Method to check if the request is live or overridden by other request
     * */
    private boolean isRequestLive(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return serviceWorkerRequestParams.equals(currentRequestParams);
    }

    /* Helper method for logging
     * */
    private void l(String msg) {
        Log.d(TAG, msg);
    }

}
