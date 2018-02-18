package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.hapramp.api.DataServer;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.CachePreference;
import com.hapramp.utils.ConnectionUtils;

/**
 * Created by Ankit on 1/21/2018.
 */

public class HomeDataManager {

    private static final String TAG = HomeDataManager.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private Context context;
    private PostLoadListener postLoadListener;
    private CachePreference cachePreference;
    private Handler mHandler;
    private String currentRequestId;

    public HomeDataManager(Context context) {
        this.context = context;
        cachePreference = CachePreference.getInstance();
        databaseHelper = new DatabaseHelper(context);
        mHandler = new Handler();
    }

    public void registerPostListeners(PostLoadListener loadListener) {
        this.postLoadListener = loadListener;
    }

    // this method is called for first laod from cache or fresh load only
    public void getPosts(final String uri, final int communityId) {

        // prepare request id
        currentRequestId = getRequestId(uri, communityId);

        //perform a check for Cache
        if (cachePreference.isPostSynced(getSegmentId(uri, communityId))) {
            // there are existing posts in the cache
            // load them on worker thread and return back
           if (postLoadListener != null) {
                l("onLoadingFromCache()");
                postLoadListener.onLoadingFromCache();
            }

            new Thread() {
                @Override
                public void run() {
                    // try loading from cache
                    final PostResponse cachedItem = databaseHelper.getCachedSegment(uri, communityId);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (cachedItem != null && isRequestLive(getRequestId(uri, communityId))) {

                                if (postLoadListener != null) {
                                    l("onFeedLoaddFromCache()");
                                    postLoadListener.onFeedLoadedFromCache(cachedItem);
                                }

                            }
                        }
                    });
                }
            }.start();

           // check for connectivity
            if (ConnectionUtils.isConnected(context)) {
                //start syncing of posts and refresh the cache
                l("onRefreshingPostFromServer");
                postLoadListener.onRefreshingPostFromServer();
                startPostSync(uri, communityId, false);

            }else{

                postLoadListener.onFreshFeedFetchError("No Internet Connectivity!");

            }

        } else {
            l("onNoFeedFoundInCache()");
            postLoadListener.onNoFeedFoundInCache();
            // there is no cache!!
            // start loading from server
            // return the results and cache them
            if (ConnectionUtils.isConnected(context)) {
                l("onRefreshingPostFromServer()");
                postLoadListener.onRefreshingPostFromServer();
                startPostSync(uri, communityId , false);

            }

        }
    }

    public void getPostForLoadMoreRequest(final String uri, final int communityId){

        currentRequestId = getRequestId(uri, communityId);

        if (ConnectionUtils.isConnected(context)) {
            l("getPostForLoadMoreRequest");
            startPostSync(uri, communityId, true);
        }else{
            postLoadListener.onAppendingFeedLoadError("No Internet Connectivity!");
        }

    }

    private void startPostSync(final String uri, final int communityId, final boolean isLoadMore) {

        if (communityId == 0) {

            DataServer.getPosts(uri, new PostFetchCallback() {
                @Override
                public void onPostFetched(PostResponse postResponses) {

                    l("PostSyncResponse "+postResponses.toString());



                    if (!isLoadMore) {
                        l("Caching Post Segment");
                        //cache items
                        databaseHelper.insertSegment(postResponses, uri, communityId);
                        cachePreference.setPostSynced(getSegmentId(uri, communityId));
                    }

                    l("Current Request Live:"+isRequestLive(getRequestId(uri, communityId)));
                    //return result
                    if (postLoadListener != null && isRequestLive(getRequestId(uri, communityId))) {

                        if (isLoadMore) {
                            l("onFeedLoadedForAppending()");
                            postLoadListener.onFeedLoadedForAppending(postResponses);
                        } else {
                            l("onFreshFeedsFetched()");
                            postLoadListener.onFreshFeedsFechted(postResponses);
                        }

                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                   if(postLoadListener!=null){
                       if(isLoadMore){
                           l("onLoadMoreFetchError()");
                           postLoadListener.onAppendingFeedLoadError("Something Went Wrong!");
                       }else{
                           l("onFreshFeedFetchError()");
                           postLoadListener.onFreshFeedFetchError("Something Went Wrong");
                       }

                   }
                }
            });

        } else {

            DataServer.getPosts(uri, communityId, new PostFetchCallback() {
                @Override
                public void onPostFetched(PostResponse postResponses) {

                    if (!isLoadMore) {

                        //cache items
                        cachePreference.setPostSynced(getSegmentId(uri, communityId));
                        databaseHelper.insertSegment(postResponses, uri, communityId);
                    }

                    //return result
                    if (postLoadListener != null && isRequestLive(getRequestId(uri, communityId))) {
                        if (isLoadMore) {
                            l("onFeedLoadedForAppending()");
                            postLoadListener.onFeedLoadedForAppending(postResponses);
                        } else {
                            l("onFreshFeedsFetched()");
                            postLoadListener.onFreshFeedsFechted(postResponses);
                        }
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if(postLoadListener!=null){
                        if(isLoadMore){
                            l("onLoadMoreFetchError()");
                            postLoadListener.onAppendingFeedLoadError("Something Went Wrong!");
                        }else{
                            l("onFreshFeedFetchError()");
                            postLoadListener.onFreshFeedFetchError("Something Went Wrong!");
                        }

                    }
                }

            });
        }
    }

    public void getFreshPosts(String postFetchStartUrl, int currentSelectedSkillId) {
        //send refreshing event
        l("getFreshPost()");
        postLoadListener.onRefreshingPostFromServer();
        //start syncing
        startPostSync(postFetchStartUrl, currentSelectedSkillId, false);
        //send back the result in onPostRefreshed()
    }

    public boolean isRequestLive(String requestId) {
        return requestId.equals(currentRequestId);
    }

    public String getRequestId(String uri, int communityId) {
        return String.valueOf("requestId_" + uri + "#" + communityId);
    }

    public String getSegmentId(String uri, int communityId) {
        return String.valueOf(uri + "#" + communityId);
    }

    private void l(String msg) {
        Log.i("HomeFeedTest"," > ["+TAG+"]  "+ msg);
    }

    public interface PostLoadListener {

        //cache
        void onLoadingFromCache();

        void onFeedLoadedFromCache(PostResponse response);

        void onNoFeedFoundInCache();

        //fresh sync from server
        void onRefreshingPostFromServer();

        void onFreshFeedsFechted(PostResponse postResponse);

        void onFreshFeedFetchError(String msg);

        // load more
        void onLoadingPostForAppending();

        void onFeedLoadedForAppending(PostResponse response);

        void onNoFeedForAppending();

        void onAppendingFeedLoadError(String msg);

    }

}
