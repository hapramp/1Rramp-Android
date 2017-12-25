package com.hapramp.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hapramp.db.UploadJobDatabaseHelper;
import com.hapramp.main.HapRampMain;
import com.hapramp.models.PostJobModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.services.PostJobDispatcherService;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Ankit on 12/23/2017.
 */

public class PostCreationController{

    private static Intent jobServiceIntent;
    private Context mContext;
    private static List<PostJobModel> postJobModels;
    private static UploadJobDatabaseHelper databaseHelper;

    private static void initDBObject(){

        if(databaseHelper==null){
            databaseHelper = new UploadJobDatabaseHelper(HapRampMain.getContext());
        }
    }

    public static void startDispatch(){

       // get the list of jobs and send them
        jobServiceIntent = new Intent(HapRampMain.getContext(),PostJobDispatcherService.class);
        postJobModels = getPendingJobs();

        for(int i=0;i<postJobModels.size();i++){
            databaseHelper.setJobStatus(postJobModels.get(i).jobId,PostJobModel.JOB_UNDER_PROCESS);
            l("dispatching job");
            jobServiceIntent.putExtra("job",postJobModels.get(i));
            HapRampMain.getContext().startService(jobServiceIntent);

        }

    }

    public static void addJob(PostJobModel postJob){

       l("added Job");
        // add job to database
         initDBObject();
         databaseHelper.insertJob(postJob);
        // startDispatch service

        // keeping a gap of 2 sec for duplicate
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startDispatch();
            }
        },2000);

    }

    private static void l(String msg){

        Log.d("SMSX",msg);

    }
    private static List<PostJobModel> getPendingJobs(){

        initDBObject();
        l("getting jobs");
        return databaseHelper.getJobs(PostJobModel.JOB_PENDING);

    }

    public static void invalidateJobs(){

        initDBObject();

        List<PostJobModel> jobModels = databaseHelper.getJobs(PostJobModel.JOB_UNDER_PROCESS);

        // if service is off, put them in right state -> Pending
        if(!HaprampPreferenceManager.getInstance().isPostingServiceRunning()){
            Log.d("PostController","service not running...Resetting the Job Status!");
            for (PostJobModel postJobModel:jobModels ) {
                databaseHelper.setJobStatus(postJobModel.jobId,PostJobModel.JOB_PENDING);
            }
        }

    }

}
