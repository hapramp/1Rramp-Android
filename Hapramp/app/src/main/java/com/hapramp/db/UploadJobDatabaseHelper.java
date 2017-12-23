package com.hapramp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hapramp.models.PostJobModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/23/2017.
 */

public class UploadJobDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jobDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "postJobs";
    private static final String KEY_JOB_ID = "jobId";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_MEDIA_URI = "media_uri";
    private static final String KEY_POST_TYPE = "post_type";
    private static final String KEY_SKILLS = "skills";
    private static final String KEY_CONTEST_ID = "contest_id";

    private static final String JOB_TABLE_STATEMENT = "CREATE TABLE " + TABLE_NAME
            + "( " +
            KEY_JOB_ID + " INTEGER"+
            KEY_CONTENT + " TEXT," +
            KEY_MEDIA_URI + " TEXT," +
            KEY_SKILLS + " TEXT," +
            KEY_POST_TYPE + " INTEGER," +
            KEY_CONTEST_ID + " INTEGER )";


    public UploadJobDatabaseHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JOB_TABLE_STATEMENT);
    }

    public void insertJob(PostJobModel postJob){

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_JOB_ID,postJob.jobId);
        values.put(KEY_CONTENT,postJob.content);
        values.put(KEY_MEDIA_URI,postJob.media_uri);
        values.put(KEY_CONTEST_ID,postJob.contest_id);
        values.put(KEY_SKILLS,postJob.getSkillsAsPaddedString());
        values.put(KEY_POST_TYPE,postJob.post_type);


        long id = database.insert(TABLE_NAME,null,values);
        if(id>-1){
            Log.d("Job"," inserted!");
        }
    }

    public void removeJob(String jobId){
        SQLiteDatabase database = getWritableDatabase();
        long id = database.delete(TABLE_NAME,KEY_JOB_ID+"=?",new String[]{jobId});
        if(id>-1){
            Log.d("Job"," deleted!");
        }

    }

    public List<PostJobModel> getJobs(){
        SQLiteDatabase database = getWritableDatabase();
        List<PostJobModel> jobs = new ArrayList<>();
        String[] cols = {
                KEY_JOB_ID ,
                KEY_CONTEST_ID ,
                KEY_POST_TYPE ,
                KEY_SKILLS ,
                KEY_MEDIA_URI ,
                KEY_CONTENT};

        Cursor cursor = database.query(TABLE_NAME,cols,null,null,null,null,null);
        boolean hasMore = cursor.getCount()>0;

        while(hasMore){
            jobs.add(new PostJobModel(
                    cursor.getInt(cursor.getColumnIndex(KEY_JOB_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_MEDIA_URI)),
                    cursor.getInt(cursor.getColumnIndex(KEY_POST_TYPE)),
                    cursor.getString(cursor.getColumnIndex(KEY_SKILLS)),
                    cursor.getInt(cursor.getColumnIndex(KEY_CONTEST_ID))
                    ));
            hasMore = cursor.moveToNext();
        }

        Log.d("Job"," fetched "+jobs.size()+" jobs");

        return jobs;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
