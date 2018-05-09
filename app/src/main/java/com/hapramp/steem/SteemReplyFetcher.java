package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.user.Profile;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.database.models.state.Discussion;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 4/15/2018.
 */

public class SteemReplyFetcher {


    private static final String TAG = SteemReplyFetcher.class.getSimpleName();
    private Handler mHandler;

    public SteemReplyFetcher() {
        this.mHandler = new Handler();
    }

    @WorkerThread
    public void requestReplyForPost(final String authorOfPost, final String permlink) {

        if (steemReplyFetchCallback != null) {
            steemReplyFetchCallback.onReplyFetching();
        }

        Log.d(TAG, "Fetching Replies");

        new Thread() {
            @Override
            public void run() {

                final SteemJ steemJ = SteemHelper.getSteemInstance();
                if (steemJ == null) {
                    mHandler.post(replyFetchFailedCallback);
                    return;
                }
                final AccountName authorAccount = new AccountName(authorOfPost);

                try {
                    final List<Discussion> discussions = steemJ.getContentReplies(authorAccount, new Permlink(permlink));
                    final List<SteemCommentModel> contentCommentModels = new ArrayList<>();
                    for (int i = 0; i < discussions.size(); i++) {
                        Discussion discussion = discussions.get(i);
                        contentCommentModels.add(new SteemCommentModel(
                                discussion.getAuthor().getName(),
                                discussion.getBody(),
                                discussion.getLastUpdate().getDateTime(),
                                ""
                        ));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (steemReplyFetchCallback != null) {
                                steemReplyFetchCallback.onReplyFetched(contentCommentModels);
                            }
                        }
                    });

                } catch (SteemCommunicationException e) {
                    Log.d(TAG, e.toString());
                    mHandler.post(replyFetchFailedCallback);
                } catch (SteemResponseException e) {
                    Log.d(TAG, e.toString());
                    mHandler.post(replyFetchFailedCallback);
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private Runnable replyFetchFailedCallback = new Runnable() {
        @Override
        public void run() {
            if (steemReplyFetchCallback != null) {
                steemReplyFetchCallback.onReplyFetchError();
            }
        }
    };

    private SteemReplyFetchCallback steemReplyFetchCallback;

    public void setSteemReplyFetchCallback(SteemReplyFetchCallback steemReplyFetchCallback) {
        this.steemReplyFetchCallback = steemReplyFetchCallback;
    }

    public interface SteemReplyFetchCallback {

        void onReplyFetching();

        void onReplyFetched(List<SteemCommentModel> replies);

        void onReplyFetchError();
    }

}
