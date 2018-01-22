package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.hapramp.api.DataServer;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.CachePreference;

/**
 * Created by Ankit on 1/21/2018.
 */

public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private Context context;
    private PostLoadListener postLoadListener;
    private CachePreference cachePreference;
    private Handler mHandler;

    public DataManager(Context context) {
        this.context = context;
        cachePreference = CachePreference.getInstance();
        databaseHelper = new DatabaseHelper(context);
        mHandler = new Handler();
    }

    public void registerPostListeners(PostLoadListener loadListener) {
        this.postLoadListener = loadListener;
    }

    public void getPosts(final String uri, final int communityId, final boolean isLoadMore) {

        if (postLoadListener != null && !isLoadMore) {
            postLoadListener.onLoading();
        }

        l("Get Post Req");
        //perform a check for Cache
        if (cachePreference.getLastPostSyncTime() != null && !isLoadMore) {
            // there are existing posts in the cache
            // load them on worker thread and return back
            l("There is existing cache");

            new Thread() {
                @Override
                public void run() {

                    final PostResponse cachedItem = databaseHelper.getCachedSegment(uri, communityId);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (cachedItem != null) {

                                l("Retrieved from cache");
                                if (postLoadListener != null) {
                                    l("Returned from cache");

                                    if (isLoadMore) {
                                        postLoadListener.onPostRefreshed(cachedItem);
                                    } else {
                                        postLoadListener.onPostLoaded(cachedItem);
                                    }

                                }

                            } else {
                                l("No Cache for :" + uri + "#" + communityId);
                            }
                        }

                    });
                }
            }.start();

            l("Starting sync");
            //start syncing of posts and refresh the cache
            startPostSync(uri, communityId, false);

        } else {
            // there is no cache!!
            // start loading from server
            // return the results and cache them
            l("Start Sync From Server");
            startPostSync(uri, communityId, isLoadMore);

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
                        cachePreference.setLastPostSyncTime(String.valueOf(SystemClock.currentThreadTimeMillis()));
                    }

                    //return result
                    if (postLoadListener != null) {
                        l("Synced Post Returned");
                        if (isLoadMore) {
                            postLoadListener.onPostLoaded(postResponses);
                        } else {
                            postLoadListener.onPostRefreshed(postResponses);
                        }
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePreference.getLastPostSyncTime() != null) {
                        if (postLoadListener != null) {
                            postLoadListener.onPostLoadError("Something went wrong while Syncing...");
                        }
                    } else {
                        if (postLoadListener != null) {
                            postLoadListener.onPostLoadError("Error While getting your Posts...");
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
                        cachePreference.setLastPostSyncTime(String.valueOf(SystemClock.currentThreadTimeMillis()));
                        databaseHelper.insertSegment(postResponses, uri, communityId);
                    }

                    //return result
                    if (postLoadListener != null) {
                        l("Synced Post Returned");
                        if (isLoadMore) {
                            postLoadListener.onPostLoaded(postResponses);
                        } else {
                            postLoadListener.onPostRefreshed(postResponses);
                        }
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePreference.getLastPostSyncTime() != null) {
                        if (postLoadListener != null) {
                            postLoadListener.onPostLoadError("Something went wrong while Syncing...");
                        }
                    } else {
                        if (postLoadListener != null) {
                            postLoadListener.onPostLoadError("Error While getting your Posts...");
                        }
                    }
                }

            });
        }
    }


    public interface PostLoadListener {

        void onPostLoaded(PostResponse response);

        void onPostLoadError(String errorMsg);

        void onLoading();

        void onPostRefreshed(PostResponse refreshedResponse);

    }

    private void l(String msg) {
        Log.i(TAG, msg);
    }
}
