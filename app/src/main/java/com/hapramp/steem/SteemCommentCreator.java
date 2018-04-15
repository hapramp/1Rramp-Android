package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.hapramp.preferences.HaprampPreferenceManager;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 4/15/2018.
 */

public class SteemCommentCreator {

    private static final String TAG = SteemCommentCreator.class.getSimpleName();
    private Handler mHandler;

    public SteemCommentCreator() {
        this.mHandler = new Handler();
    }

    @WorkerThread
    public void createComment(final String comment, final String commentOnUser, final String __permlink) {

        if (steemCommentCreateCallback != null) {
            steemCommentCreateCallback.onCommentCreateProcessing();
        }

        Log.d(TAG, "Creating Comment");

        new Thread() {
            @Override
            public void run() {

                final SteemJ steemJ = SteemHelper.getSteemInstance();
                if (steemJ == null) {
                    mHandler.post(commentCreateFailedRunnable);
                    return;
                }

                final AccountName commenter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                final AccountName commentOnUserAccount = new AccountName(commentOnUser);
                final String[] tags = {"hapramp"};

                try {
                    CommentOperation commentOperation = steemJ.createComment(commenter, commentOnUserAccount, new Permlink(__permlink), comment, tags);
                    //if (commentOperation.getPermlink() != null) {
                    Log.d(TAG, "Comment created " + commentOperation.getPermlink());
                    mHandler.post(commentCreatedRunnable);
                    // }
                } catch (SteemCommunicationException e) {
                    Log.d(TAG, e.toString());
                    mHandler.post(commentCreateFailedRunnable);
                } catch (SteemResponseException e) {
                    Log.d(TAG, e.toString());
                    mHandler.post(commentCreateFailedRunnable);
                    e.printStackTrace();
                } catch (SteemInvalidTransactionException e) {
                    Log.d(TAG, e.toString());
                    mHandler.post(commentCreateFailedRunnable);
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private Runnable commentCreatedRunnable = new Runnable() {
        @Override
        public void run() {
            if (steemCommentCreateCallback != null) {
                steemCommentCreateCallback.onCommentCreated();
            }
        }
    };

    private Runnable commentCreateFailedRunnable = new Runnable() {
        @Override
        public void run() {
            if (steemCommentCreateCallback != null) {
                steemCommentCreateCallback.onCommentCreateFailed();
            }
        }
    };

    private SteemCommentCreateCallback steemCommentCreateCallback;

    public void setSteemCommentCreateCallback(SteemCommentCreateCallback steemCommentCreateCallback) {
        this.steemCommentCreateCallback = steemCommentCreateCallback;
    }

    public interface SteemCommentCreateCallback {
        void onCommentCreateProcessing();

        void onCommentCreated();

        void onCommentCreateFailed();
    }
}

