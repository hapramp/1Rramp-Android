package com.hapramp.dao;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hapramp.datamodels.CommentModel;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.steem.models.FeedResponse;

import java.util.List;

/**
 * Created by Ankit on 5/9/2018.
 */

@Dao
public interface CommentsDao {

    @Insert
    public void insert(SteemCommentModel comment);

    @Query("DELETE FROM comments")
    public void deleteAll();

    @Query("SELECT * FROM comments WHERE postPermlink = :postPermlink")
    public List<SteemCommentModel> getComments(String postPermlink);

}
