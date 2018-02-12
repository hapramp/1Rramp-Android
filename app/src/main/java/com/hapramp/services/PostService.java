package com.hapramp.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.api.DataServer;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.models.PostJobModel;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 1/20/2018.
 */

public class PostService extends JobService implements PostCreateCallback {

    private static final String TAG = PostService.class.getSimpleName();
    private static final int MAX_LENGTH = 64;
    private PostJobModel currentJob;
    List<PostJobModel> postJobModels;
    private DatabaseHelper databaseHelper;
    private FirebaseStorage storage;
    private JobParameters jobParameters;
    private final String rootFolder = "images";

    @Override
    public void onCreate() {

        super.onCreate();
        postJobModels = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        databaseHelper = new DatabaseHelper(this);

    }

    @Override
    public boolean onStartJob(JobParameters job) {

        HaprampPreferenceManager.getInstance().setPostingServiceRunning(true);
        l("Getting job ");
        this.jobParameters = job;
        fetchAndStartDispatching();
        return true;

    }

    private void dispatch(PostJobModel postJob) {

        currentJob = postJob;
        if (postJob.media_uri.length() > 0) {
            uploadMedia(postJob.jobId, postJob.media_uri);
        } else {
            uploadPost(postJob.jobId, "");
        }

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        l("Stopping service");
        HaprampPreferenceManager.getInstance().setPostingServiceRunning(false);
        return postJobModels.size() > 0;

    }



    private void uploadMedia(final String jobId, String uri) {

        l("Uploading Media");
        StorageReference storageRef = storage.getReference();
        StorageReference ref =
                storageRef
                .child(rootFolder)
                .child(PostJobModel.getMediaLocation());

        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final UploadTask uploadTask = ref.putStream(stream);

        Handler handler = new Handler(getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        uploadPost(jobId, downloadUrl.toString());
                    }
                });
            }
        });

    }

    private void uploadPost(String jobId, String uploadedMediaUri) {

        l("Uploading Post");
        String media_url = uploadedMediaUri != null ? uploadedMediaUri.toString() : "";
        PostCreateBody body = new PostCreateBody(
                currentJob.content,
                media_url,
                currentJob.post_type,
                currentJob.skills,
                null);

        DataServer.createPost(jobId, body, this);

    }


    @Override
    public void onPostCreated(String... jobId) {

        sendBroadcast();
        l("Removing job " + jobId + " From DB");
        databaseHelper.removeJob(String.valueOf(jobId));
        l("Removing job " + jobId + " From local list");
        postJobModels.remove(0);

        if (postJobModels.size() > 0) {
            l("dispatching job " + postJobModels.get(0).jobId);
            dispatch(postJobModels.get(0));
        } else {
            jobFinished(jobParameters, false);
            HaprampPreferenceManager.getInstance().setPostingServiceRunning(false);
        }


        // start again
        fetchAndStartDispatching();

    }

    @Override
    public void onPostCreateError(String... jobId) {
        //retry the jobs
        l("Something went wrong, we will try again");
        jobFinished(jobParameters, true);

    }

    private void fetchAndStartDispatching() {

        if(HaprampPreferenceManager.getInstance().isNewJobAvailble()) {
            l("Refreshing Jobs");

            postJobModels = databaseHelper.getJobs(PostJobModel.JOB_PENDING);

            if(postJobModels.size()>0) {
                dispatch(postJobModels.get(0));
            }else{
                l("All Done!!!");
            }

        }

        //reset new jobs
        HaprampPreferenceManager.getInstance().setJobAvailable(false);

    }

    private static void l(String msg) {
        Log.d(TAG, msg);
    }

    private void sendBroadcast(){
        Intent intent = new Intent(Constants.ACTION_POST_UPLOAD);
        sendBroadcast(intent);
    }



}