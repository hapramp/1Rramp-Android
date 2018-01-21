package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import com.hapramp.api.DataServer;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.CachePrefernce;
import com.hapramp.utils.MomentsUtils;

/**
 * Created by Ankit on 1/21/2018.
 */

public class DataManager {

    private static final String TAG = DataManager.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private Context context;
    private PostLoadListener postLoadListener;
    CachePrefernce cachePrefernce;
    private Handler mHandler;

    public DataManager(Context context) {
        this.context = context;
        cachePrefernce = CachePrefernce.getInstance();
        mHandler = new Handler();
    }

    public void registerPostListeners(PostLoadListener loadListener) {
        this.postLoadListener = loadListener;
    }

    public void getPosts(final String uri, final int communityId) {

        if (postLoadListener != null) {
            postLoadListener.onLoading();
        }

        //perform a check for Cache
        if (cachePrefernce.getLastPostSyncTime() != null) {
            // there are existing posts in the cache
            // load them on worker thread and return back

            new Thread() {
                @Override
                public void run() {

                    final PostResponse cachedItem = databaseHelper.getCachedSegment(uri, communityId);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (postLoadListener != null) {
                                postLoadListener.onPostLoaded(cachedItem);
                            }
                        }

                    });
                }
            }.start();

            //start syncing of posts and refresh the cache
            startPostSync(uri, communityId);

        } else {
            // there is no cache!!
            // start loading from server
            // return the results and cache them
            startPostSync(uri, communityId);

        }
    }

    private void startPostSync(final String uri, final int communityId) {

        if (communityId == 0) {

            DataServer.getPosts(uri, new PostFetchCallback() {
                @Override
                public void onPostFetched(PostResponse postResponses) {
                    //cache items
                    cachePrefernce.setLastPostSyncTime(String.valueOf(SystemClock.currentThreadTimeMillis()));
                    databaseHelper.insertSegment(postResponses, uri, communityId);
                    //return result
                    if (postLoadListener != null) {
                        postLoadListener.onPostRefreshed(postResponses);
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePrefernce.getLastPostSyncTime() != null) {
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
                    //cache items
                    cachePrefernce.setLastPostSyncTime(String.valueOf(SystemClock.currentThreadTimeMillis()));
                    databaseHelper.insertSegment(postResponses, uri, communityId);
                    //return result
                    if (postLoadListener != null) {
                        postLoadListener.onPostRefreshed(postResponses);
                    }
                }

                @Override
                public void onPostFetchError() {
                    // report error
                    if (cachePrefernce.getLastPostSyncTime() != null) {
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

}
