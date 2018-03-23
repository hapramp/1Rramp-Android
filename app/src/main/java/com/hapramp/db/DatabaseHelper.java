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
import com.hapramp.preferences.CachePreference;
import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    private long insertFeed(ArrayList<Feed> steemFeeds, String communityTag) {

        if (wasFeedCached(communityTag)) {
            return updateFeed(steemFeeds, communityTag);
        }

        SQLiteDatabase database = getWritableDatabase();
        FeedCacheWrapper feedCacheWrapper = new FeedCacheWrapper(steemFeeds);
        long id = database.insert(TABLE_CACHE, null, getFeedCVObject(feedCacheWrapper, communityTag));
        if (id > -1) {
            Log.d(TAG, "Inserted! " + communityTag);
            CachePreference.getInstance().setCommunityFeedCached(communityTag, true);
        } else {
            Log.d(TAG, "Error While Inserting! " + communityTag);
        }

        return id;
    }

    public void insertFeeds(ArrayList<Feed> feeds) {
        // filter the feeds and insert them accordingly
        HashMap<String, ArrayList<Feed>> filterMap = new HashMap<>();
        ArrayList<Feed> tempFeedList;

        for (int i = 0; i < feeds.size(); i++) {
            // get feedlist from map
            tempFeedList = filterMap.get(feeds.get(i).getCategory()) != null ? filterMap.get(feeds.get(i).getCategory()) : new ArrayList<Feed>();
            //add feed
            tempFeedList.add(feeds.get(i));
            // put back to map
            filterMap.put(feeds.get(i).getCategory(), tempFeedList);
        }

        // TODO: 3/21/2018 loop through each category and insert to database
        Iterator iterator = filterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            insertFeed((ArrayList<Feed>) entry.getKey(), (String) entry.getValue());
        }

        CachePreference.getInstance().setAllFeedCached(true);

    }

    public ArrayList<Feed> getFeedsByCommunity(String communityTag) {

        ArrayList<Feed> feeds = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] cols = {KEY_COMMUNITY_ID, KEY_JSON};
        String[] selection = {communityTag};
        Cursor cursor = sqLiteDatabase.query(TABLE_CACHE, cols, KEY_COMMUNITY_ID + " = ? ", selection, null, null, null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        if (count > 0) {
            FeedCacheWrapper wrapper = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(KEY_JSON)), FeedCacheWrapper.class);
            feeds = wrapper.getSteemFeedModels();
        }
        Log.d(TAG, "Returning [" + feeds.size() + "] feeds of type :" + communityTag);

        return feeds;

    }

    public ArrayList<Feed> getAllFeeds(){

        ArrayList<Feed> feeds = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String[] cols = {KEY_COMMUNITY_ID, KEY_JSON};
        Cursor cursor = sqLiteDatabase.query(TABLE_CACHE, cols, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.moveToFirst();

        if (count > 0) {
            FeedCacheWrapper wrapper = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(KEY_JSON)), FeedCacheWrapper.class);
            feeds = wrapper.getSteemFeedModels();
        }

        Log.d(TAG, " Returning [" + feeds.size() + "] feeds of all type ");
        return feeds;

    }

    public boolean wasFeedCached(String comId) {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] cols = {KEY_COMMUNITY_ID, KEY_JSON};
        String[] selection = {comId};
        Cursor cursor = sqLiteDatabase.query(TABLE_CACHE, cols, KEY_COMMUNITY_ID + " = ? ", selection, null, null, null);
        int count = cursor.getCount();

        return count > 0;

    }

    public long updateFeed(ArrayList<Feed> steemFeeds, String communityId) {

        SQLiteDatabase database = getWritableDatabase();
        FeedCacheWrapper feedCacheWrapper = new FeedCacheWrapper(steemFeeds);
        long id = database.update(TABLE_CACHE, getFeedCVObject(feedCacheWrapper, communityId), KEY_COMMUNITY_ID + "= ? ", new String[]{communityId});
        if (id > -1) {
            Log.d(TAG, "Updated! " + communityId);
        } else {
            Log.d(TAG, "Error While Updating! " + communityId);
        }
        return id;
    }

    public void deleteFeedsCache() {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_CACHE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    class FeedCacheWrapper {

        @Expose
        @SerializedName("feeds")
        ArrayList<Feed> steemFeedModels;

        public FeedCacheWrapper(ArrayList<Feed> steemFeedModels) {
            this.steemFeedModels = steemFeedModels;
        }

        public ArrayList<Feed> getSteemFeedModels() {
            return steemFeedModels;
        }

        public void setSteemFeedModels(ArrayList<Feed> steemFeedModels) {
            this.steemFeedModels = steemFeedModels;
        }
    }

}
