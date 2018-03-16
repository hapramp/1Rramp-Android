package com.hapramp.interfaces.datatore_callback;

import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.List;

public interface ServiceWorkerCallback {

    //loading process, It shows the request to the service worker first time.
    void onFetchingFromServer();

    //fetching fresh data from server.
    void onRefreshing();

    //failure in loading from cache
    void onCacheLoadFailed();

    //no data in the cache
    void onNoDataInCache();

    //cache data loaded
    void onLoadedFromCache(ArrayList<Feed> cachedList);

    // data refreshed from server
    void onRefreshed(List<Object> refreshedList);

    //failed to refresh from server
    void onRefreshFailed();

    //loaded appendable data ( data on request for lazy loading)
    void onAppendableDataLoaded(List<Object> appendableList);

    //failed to load appendable data
    void onAppendableDataLoadingFailed();

    void onFeedsFetched(ArrayList<Feed> body);

    void onFetchingFromServerFailed();

}
