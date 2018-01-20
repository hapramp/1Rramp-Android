package com.hapramp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hapramp.controller.PostCreationController;
import com.hapramp.models.PostJobModel;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/23/2017.
 */

public class UploadJobDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jobDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "postJobs";
    private static final String KEY_JOB_STATUS = "jobStatus";
    private static final String KEY_JOB_ID = "jobId";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_MEDIA_URI = "media_uri";
    private static final String KEY_POST_TYPE = "post_type";
    private static final String KEY_SKILLS = "skills";
    private static final String KEY_CONTEST_ID = "contest_id";

    private static final String JOB_TABLE_STATEMENT = "CREATE TABLE " + TABLE_NAME
            + "( " +
            KEY_JOB_ID + " TEXT," +
            KEY_CONTENT + " TEXT," +
            KEY_MEDIA_URI + " TEXT," +
            KEY_SKILLS + " TEXT," +
            KEY_POST_TYPE + " INTEGER," +
            KEY_CONTEST_ID + " INTEGER," +
            KEY_JOB_STATUS + " INTEGER )";


    public UploadJobDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JOB_TABLE_STATEMENT);
    }

    private ContentValues getCVObject(PostJobModel postJob){

        ContentValues values = new ContentValues();
        values.put(KEY_JOB_ID, postJob.jobId);
        values.put(KEY_JOB_STATUS, postJob.jobStatus);
        values.put(KEY_CONTENT, postJob.content);
        values.put(KEY_MEDIA_URI, postJob.media_uri);
        values.put(KEY_CONTEST_ID, postJob.contest_id);
        values.put(KEY_SKILLS, postJob.getSkillsAsPaddedString());
        values.put(KEY_POST_TYPE, postJob.post_type);

        return values;
    }

    public void insertJob(PostJobModel postJob) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = getCVObject(postJob);

        long id = database.insert(TABLE_NAME, null, values);
        if (id > -1) {
            Log.d("Job", " inserted!");
            // set new jobs added
            HaprampPreferenceManager.getInstance().setJobAvailable(true);
        }
    }

    public void removeJob(String jobId) {
        SQLiteDatabase database = getWritableDatabase();
        long id = database.delete(TABLE_NAME, KEY_JOB_ID + "=?", new String[]{jobId});
        if (id > -1) {
            Log.d("Job", " deleted!");
        }

    }

    public List<PostJobModel> getJobs(@PostJobModel.JobStatus int jobStatus) {

        SQLiteDatabase database = getWritableDatabase();
        List<PostJobModel> jobs = new ArrayList<>();
        String[] cols = {
                KEY_JOB_ID,
                KEY_CONTEST_ID,
                KEY_POST_TYPE,
                KEY_SKILLS,
                KEY_MEDIA_URI,
                KEY_CONTENT,
                KEY_JOB_STATUS};

        Cursor cursor = database.query(TABLE_NAME, cols, KEY_JOB_STATUS+"=?", new String[]{String.valueOf(jobStatus)}, null, null, null);
        cursor.moveToFirst();
        boolean hasMore = cursor.getCount() > 0;
        while (hasMore) {
            jobs.add(new PostJobModel(
                    cursor.getString(cursor.getColumnIndex(KEY_JOB_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_MEDIA_URI)),
                    cursor.getInt(cursor.getColumnIndex(KEY_POST_TYPE)),
                    cursor.getString(cursor.getColumnIndex(KEY_SKILLS)),
                    cursor.getInt(cursor.getColumnIndex(KEY_CONTEST_ID)),
                    cursor.getInt(cursor.getColumnIndex(KEY_JOB_STATUS))
            ));
            hasMore = cursor.moveToNext();
        }

        Log.d("Job", " fetched " + jobs.size() + " jobs");

        return jobs;
    }

    public PostJobModel getJob(String jobId) {

        SQLiteDatabase database = getWritableDatabase();
        PostJobModel job = null;
        String[] cols = {
                KEY_JOB_ID,
                KEY_CONTEST_ID,
                KEY_POST_TYPE,
                KEY_SKILLS,
                KEY_MEDIA_URI,
                KEY_CONTENT,
                KEY_JOB_STATUS};

        Cursor cursor = database.query(TABLE_NAME, cols, KEY_JOB_ID+"=?", new String[]{String.valueOf(jobId)}, null, null, null);
        cursor.moveToFirst();
        boolean hasMore = cursor.getCount() > 0;

        if (hasMore) {
            job = new PostJobModel(
                    cursor.getString(cursor.getColumnIndex(KEY_JOB_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_MEDIA_URI)),
                    cursor.getInt(cursor.getColumnIndex(KEY_POST_TYPE)),
                    cursor.getString(cursor.getColumnIndex(KEY_SKILLS)),
                    cursor.getInt(cursor.getColumnIndex(KEY_CONTEST_ID)),
                    cursor.getInt(cursor.getColumnIndex(KEY_JOB_STATUS))
            );

        }

        return job;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void setJobStatus(String jobId, @PostJobModel.JobStatus int jobStatus){

        SQLiteDatabase database = getWritableDatabase();
        PostJobModel postJob = getJob(jobId);
        postJob.jobStatus = jobStatus;
        database.update(TABLE_NAME,getCVObject(postJob),KEY_JOB_ID+"=?",new String[]{String.valueOf(jobId)});

    }

}
