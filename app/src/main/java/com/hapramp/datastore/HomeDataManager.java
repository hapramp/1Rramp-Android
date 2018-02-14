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

    public void getPosts(final String uri, final int communityId, final boolean isLoadMore) {

        currentRequestId = getRequestId(uri, communityId);


        //perform a check for Cache
        if (cachePreference.isPostSynced(getSegmentId(uri, communityId)) && !isLoadMore) {
            // there are existing posts in the cache
            // load them on worker thread and return back
            l("There is existing cache");

            if (postLoadListener != null && !isLoadMore) {
                postLoadListener.onLoadingFromCache();
            }

            new Thread() {
                @Override
                public void run() {

                    final PostResponse cachedItem = databaseHelper.getCachedSegment(uri, communityId);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (cachedItem != null && isRequestLive(getRequestId(uri, communityId))) {

                                if (postLoadListener != null) {
                                    postLoadListener.onFeedLoadedFromCache(cachedItem);
                                }
                            }
                        }
                    });
                }
            }.start();

            if (ConnectionUtils.isConnected(context)) {
                l("Starting sync");
                //start syncing of posts and refresh the cache
                postLoadListener.onRefreshingPostFromServer();
                startPostSync(uri, communityId, false);
            }

        } else {

            postLoadListener.onNoFeedFoundInCache();
            // there is no cache!!
            // start loading from server
            // return the results and cache them
            if (ConnectionUtils.isConnected(context)) {
                l("Start Sync From Server");
                postLoadListener.onRefreshingPostFromServer();
                startPostSync(uri, communityId, isLoadMore);

            }

        }
    }

    private void startPostSync(final String uri, final int communityId, final boolean isLoadMore) {

        if (communityId == 0) {

            DataServer.getPosts(uri, new PostFetchCallback() {
                @Override
                public void onPostFetched(PostResponse postResponses) {

                    if (!isLoadMore) {
                        l("Caching Post Segment");
                        //cache items
                        databaseHelper.insertSegment(postResponses, uri, communityId);
                        cachePreference.setPostSynced(getSegmentId(uri, communityId));
                    }

                    //return result
                    if (postLoadListener != null && isRequestLive(getRequestId(uri, communityId))) {
                        l("Synced Post Returned");
                        if (isLoadMore) {
                            postLoadListener.onFeedLoadedForAppending(postResponses);
                        } else {
                            postLoadListener.onFreshFeedsFechted(postResponses);
                        }
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePreference.isPostSynced(getSegmentId(uri, communityId))) {
                        if (postLoadListener != null) {
                            postLoadListener.onFreshFeedFetchError();
                        }
                    } else {
                        if (postLoadListener != null) {
                            postLoadListener.onFreshFeedFetchError();
                        }
                    }
                }
            });

        } else {

            DataServer.getPosts(uri, communityId, new PostFetchCallback() {
                @Override
                public void onPostFetched(PostResponse postResponses) {

                    if (!isLoadMore) {
                        l("Caching Post Segment");
                        //cache items
                        cachePreference.setPostSynced(getSegmentId(uri, communityId));
                        databaseHelper.insertSegment(postResponses, uri, communityId);
                    }

                    //return result
                    if (postLoadListener != null && isRequestLive(getRequestId(uri, communityId))) {
                        l("Synced Post Returned");
                        if (isLoadMore) {
                            postLoadListener.onFeedLoadedForAppending(postResponses);
                        } else {
                            postLoadListener.onFreshFeedsFechted(postResponses);
                        }
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePreference.isPostSynced(getSegmentId(uri, communityId))) {
                        if (postLoadListener != null) {
                            postLoadListener.onFreshFeedFetchError();
                        }
                    } else {
                        if (postLoadListener != null) {
                            postLoadListener.onFreshFeedFetchError();
                        }
                    }
                }

            });
        }
    }

    public void getFreshPosts(String postFetchStartUrl, int currentSelectedSkillId) {
        //send refreshing event
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
        Log.i(TAG, msg);
    }


    public interface PostLoadListener {

        //cache
        void onLoadingFromCache();

        void onFeedLoadedFromCache(PostResponse response);

        void onNoFeedFoundInCache();

        //fresh sync from server
        void onRefreshingPostFromServer();

        void onFreshFeedsFechted(PostResponse postResponse);

        void onFreshFeedFetchError();

        // load more
        void onLoadingPostForAppending();

        void onFeedLoadedForAppending(PostResponse response);

        void onNoFeedForAppending();

    }

}
