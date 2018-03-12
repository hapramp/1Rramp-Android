package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.CachePreference;
import com.hapramp.steem.ServiceWorkerRequestParams;

public class ServiceWorker {

    private static final String TAG  = ServiceWorker.class.getSimpleName();
    ServiceWorkerRequestParams requestParams;
    ServiceWorkerCallback serviceWorkerCallback;
    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private Handler mHandler;
    private CachePreference mCachePreference;
    private ServiceWorker serviceWorkerInstance;

    private ServiceWorkerRequestParams currentRequestParams;

    // setup all the deps
    public void init(Context context){

        this.mContext = context;
        mCachePreference = CachePreference.getInstance();
        mDatabaseHelper = new DatabaseHelper(context);
        mHandler = new Handler();

    }

    public void setServiceWorkerCallback(ServiceWorkerCallback serviceWorkerCallback) {
        this.serviceWorkerCallback = serviceWorkerCallback;
    }

    //method to get feeds(cached or fresh)
    void requestFeeds(ServiceWorkerRequestParams requestParams){
        this.currentRequestParams = requestParams;

        // TODO: 2/28/2018
        // 1 - check for the cache, if found return else report its absence
        if(mCachePreference.wasFeedCached()){
            // read from databse and return
            // and start refreshing...
        }else{
            // no cache
            // start refreshing
        }
        // 2 - start refreshing feed, and call the refreshing callback
        // 3 - onNetwork response call feed refreshed/onFeedRefreshError
        // 4 - cache the refreshed feeds

    }

    //method to request lazy loading
    void requestAppendableFeed(ServiceWorkerRequestParams requestParams){
        this.currentRequestParams = requestParams;

        // TODO: 2/28/2018
        // 1 - call api for feeds
        // onSucess: isRequestLive() --> onAppendableDataFetched()
        // onFailed: isRequestLive() --> onAppendableFetchFailed()

    }

    //method to request feed refresh
    void refreshFeeds(ServiceWorkerRequestParams requestParams){
        this.currentRequestParams = requestParams;

        // TODO: 2/28/2018
        // 1 - call api for feeds
        // onSucess: isRequestLive() --> onRefreshed()
        // onFailed: isRequestLive() --> onRefreshFailed
        // cache the refreshed feeds


    }

    private void fetchFeeds(ServiceWorkerRequestParams feedRequestParams){

        // TODO: 2/28/2018
        // 1 - call api for feeds
        // onSucess: isRequestLive() --> onDataLoaded() --> cache
        // onFailed: isRequestLive() --> onDataLoadFailed()

    }

    private boolean isRequestLive(ServiceWorkerRequestParams serviceWorkerRequestParams){
        return serviceWorkerRequestParams.equals(currentRequestParams);
    }

    private void l(String msg){
        Log.d(TAG,msg);
    }

}
