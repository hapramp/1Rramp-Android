package com.hapramp.controller;

import com.hapramp.db.UploadJobDatabaseHelper;
import com.hapramp.main.HapRampMain;
import com.hapramp.models.PostJobModel;
import com.hapramp.services.JobManager;

/**
 * Created by Ankit on 12/23/2017.
 */

public class PostCreationController{

    private static UploadJobDatabaseHelper databaseHelper;

    private static void initDBObject(){

        if(databaseHelper==null){
            databaseHelper = new UploadJobDatabaseHelper(HapRampMain.getContext());
        }
    }

    public static void addJob(PostJobModel postJob){

        // add job to database
         initDBObject();
         databaseHelper.insertJob(postJob);
        // startDispatch
        JobManager.init();

    }


}
