package com.hapramp.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.hapramp.dao.CommentsDao;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.steem.models.FeedResponse;

/**
 * Created by Ankit on 5/9/2018.
 */

@Database(entities = {SteemCommentModel.class}, version = 1)
public abstract class HaprampRoomDatabase extends RoomDatabase {
    public abstract CommentsDao getCommentsDao();
    private static HaprampRoomDatabase INSTANCE;
    public static HaprampRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HaprampRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HaprampRoomDatabase.class, "hapramp_cache_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
