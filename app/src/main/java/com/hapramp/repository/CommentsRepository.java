package com.hapramp.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.hapramp.dao.CommentsDao;
import com.hapramp.datamodels.CommentModel;
import com.hapramp.roomdb.HaprampRoomDatabase;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.steem.SteemReplyFetcher;
import com.hapramp.steem.models.FeedResponse;
import com.hapramp.utils.ConnectionUtils;

import java.util.List;

/**
 * Created by Ankit on 5/9/2018.
 */

public class CommentsRepository implements SteemReplyFetcher.SteemReplyFetchCallback {

    private final SteemReplyFetcher steemReplyFetcher;
    private CommentsDao commentsDao;
    private Handler mHandler;
    private Context context;
    private MutableLiveData<List<SteemCommentModel>> comments;

    public CommentsRepository(Application application) {
        this.context = application;
        HaprampRoomDatabase db = HaprampRoomDatabase.getDatabase(application);
        steemReplyFetcher = new SteemReplyFetcher();
        steemReplyFetcher.setSteemReplyFetchCallback(this);
        mHandler = new Handler();
        commentsDao = db.getCommentsDao();
    }

    public MutableLiveData<List<SteemCommentModel>> getComments(String author, final String postPermlink) {
        if (comments == null) {
            comments = new MutableLiveData<>();
        }
        new Thread() {
            @Override
            public void run() {
                final List<SteemCommentModel> cmts = commentsDao.getComments(postPermlink);
                Log.d("RoomData","From Db:"+cmts.size());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        comments.setValue(cmts);
                    }
                });
            }
        }.start();

        fetchComments(author, postPermlink);
        return comments;
    }

    private void fetchComments(String author, String postPermlink) {
        if(ConnectionUtils.isConnected(context)) {
            steemReplyFetcher.requestReplyForPost(author, postPermlink);
        }
    }

    @Override
    public void onReplyFetching() {

    }

    @Override
    public void onReplyFetched(final List<SteemCommentModel> replies) {
        comments.setValue(replies);
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<replies.size();i++) {
                    commentsDao.insert(replies.get(i));
                }
            }
        }.start();
    }

    @Override
    public void onReplyFetchError() {

    }

    public void addComment(final SteemCommentModel commentToAdd) {
        new Thread() {
            @Override
            public void run() {
                commentsDao.insert(commentToAdd);
                final List<SteemCommentModel> steemCommentModels = comments.getValue();
                steemCommentModels.add(commentToAdd);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        comments.setValue(steemCommentModels);
                    }
                });
            }
        }.start();
    }
}
