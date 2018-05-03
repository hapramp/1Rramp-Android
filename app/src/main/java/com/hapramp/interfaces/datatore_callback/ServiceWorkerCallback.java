package com.hapramp.interfaces.datatore_callback;

import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.List;

public interface ServiceWorkerCallback {


    //=========================================
    // CACHE CALLBACK
    //=========================================

    //process of loading from cache
    void onLoadingFromCache();

    //failure in loading from cache
    void onCacheLoadFailed();

    //no data in the cache
    void onNoDataInCache();

    //cache data loaded
    void onLoadedFromCache(ArrayList<Feed> cachedList, String lastAuthor, String lastPermlink);


    //=========================================
    // SERVER FETCH CALLBACK
    //=========================================

    //loading process, It shows the request to the service worker first time.
    void onFetchingFromServer();

    // fetched from server
    void onFeedsFetched(ArrayList<Feed> body, String lastAuthor, String lastPermlink);

    //something went wrong while fetching from server
    void onFetchingFromServerFailed();


    //=========================================
    // REFRESHING CALLBACK
    //=========================================


    //fetching fresh data from server.
    void onRefreshing();

    // data refreshed from server
    void onRefreshed(List<Feed> refreshedList, String lastAuthor, String lastPermlink);

    //failed to refresh from server
    void onRefreshFailed();


    //=========================================
    // LAZY LOADING CALLBACK
    //=========================================
    // Note: Here appendable data means the feeds which are going to be appended
    // to last of list as user is scrolling through.

    // process of loading appendable data
    void onLoadingAppendableData();

    //loaded appendable data ( data on request for lazy loading)
    void onAppendableDataLoaded(List<Feed> appendableList, String lastAuthor, String lastPermlink);

    //failed to load appendable data
    void onAppendableDataLoadingFailed();

}
