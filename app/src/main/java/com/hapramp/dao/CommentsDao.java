package com.hapramp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.hapramp.steem.SteemCommentModel;

import java.util.List;

/**
 * Created by Ankit on 5/9/2018.
 */

@Dao
public interface CommentsDao {

  @Insert
  void insert(SteemCommentModel comment);

  @Query("SELECT * FROM comments WHERE postPermlink = :postPermlink")
  List<SteemCommentModel> getComments(String postPermlink);

}
