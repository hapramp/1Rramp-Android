package com.hapramp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.models.Feed;
import com.hapramp.models.PostJobModel;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.SteemFeedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/23/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "feedsDB";
    private static final String KEY_COMMUNITY_ID = "comId";
    private static final String KEY_JSON = "json";
    private static final int DB_VERSION = 1;
    private static final String TABLE_CACHE = "feeds";


    private static final String POST_CACHE_TABLE_STATEMENT = "CREATE TABLE " + TABLE_CACHE
            + "( " +
            KEY_COMMUNITY_ID + " TEXT," +
            KEY_JSON + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POST_CACHE_TABLE_STATEMENT);
    }

    private ContentValues getFeedCVObject(FeedCacheWrapper feedCacheWrapper, String communityId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMMUNITY_ID, communityId);
        contentValues.put(KEY_JSON, new Gson().toJson(feedCacheWrapper));
        return contentValues;

    }

    public long insertFeed(ArrayList<SteemFeedModel> steemFeeds, String communityId) {

        if(wasFeedCached(communityId)){
            return updateFeed(steemFeeds,communityId);
        }

        SQLiteDatabase database = getWritableDatabase();
        FeedCacheWrapper feedCacheWrapper = new FeedCacheWrapper(steemFeeds);
        long id = database.insert(TABLE_CACHE, null, getFeedCVObject(feedCacheWrapper, communityId));
        if (id > -1) {
            Log.d(TAG, "Inserted! " + communityId);
        } else {
            Log.d(TAG, "Error While Inserting! " + communityId);
        }

        return id;
    }

    public ArrayList<SteemFeedModel> getFeed(String comId) {

        ArrayList<SteemFeedModel> feeds = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] cols = {KEY_COMMUNITY_ID, KEY_JSON};
        String[] selection = {comId};
        Cursor cursor = sqLiteDatabase.query(TABLE_CACHE, cols, KEY_COMMUNITY_ID + " = ? ", selection, null, null, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        if (count > 0) {
            FeedCacheWrapper wrapper = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(KEY_JSON)),FeedCacheWrapper.class);
            feeds = wrapper.getSteemFeedModels();
        }
        Log.d(TAG,"Returning ["+feeds.size()+"] feeds of type :"+comId);

        return feeds;

    }

    public boolean wasFeedCached(String comId){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] cols = {KEY_COMMUNITY_ID, KEY_JSON};
        String[] selection = {comId};
        Cursor cursor = sqLiteDatabase.query(TABLE_CACHE, cols, KEY_COMMUNITY_ID + " = ? ", selection, null, null, null);
        int count = cursor.getCount();

        return count>0;

    }

    public long updateFeed(ArrayList<SteemFeedModel> steemFeeds, String communityId){

        SQLiteDatabase database = getWritableDatabase();
        FeedCacheWrapper feedCacheWrapper = new FeedCacheWrapper(steemFeeds);
        long id = database.update(TABLE_CACHE, getFeedCVObject(feedCacheWrapper, communityId),KEY_COMMUNITY_ID+"= ? ", new String[]{communityId});
        if (id > -1) {
            Log.d(TAG, "Updated! " + communityId);
        } else {
            Log.d(TAG, "Error While Updating! " + communityId);
        }
        return id;
    }

    public void deleteFeedsCache(){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+TABLE_CACHE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    class FeedCacheWrapper{

        @Expose
        @SerializedName("feeds")
        ArrayList<SteemFeedModel> steemFeedModels;

        public FeedCacheWrapper(ArrayList<SteemFeedModel> steemFeedModels) {
            this.steemFeedModels = steemFeedModels;
        }

        public ArrayList<SteemFeedModel> getSteemFeedModels() {
            return steemFeedModels;
        }

        public void setSteemFeedModels(ArrayList<SteemFeedModel> steemFeedModels) {
            this.steemFeedModels = steemFeedModels;
        }
    }

}
