package com.hapramp.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.controller.PostCreationController;
import com.hapramp.db.UploadJobDatabaseHelper;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.PostJobModel;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Ankit on 12/23/2017.
 */

public class PostJobDispatcherService extends Service implements PostCreateCallback {

    private static final String TAG = PostJobDispatcherService.class.getSimpleName();
    private FirebaseStorage storage;
    private PostJobModel postJob;
    private final String rootFolder = "images";
    private UploadJobDatabaseHelper databaseHelper;


    public PostJobDispatcherService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        l("Created Service");
        databaseHelper = new UploadJobDatabaseHelper(this);
        storage = FirebaseStorage.getInstance();
        HaprampPreferenceManager.getInstance().setPostingServiceRunning(true);

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        l("received job");
        postJob = intent.getParcelableExtra("job");
        l("working on job...");
        if (postJob.media_uri.length() > 0) {
            uploadMedia(postJob.jobId, postJob.media_uri);
        } else {
            uploadPost(postJob.jobId, "");
        }

        return START_STICKY;
    }


    private void uploadMedia(final String jobId, String uri) {

        l("Uploading Media");

        sendStatusBroadcast("Uploading Media : "+uri);

        StorageReference storageRef = storage.getReference();
        // Store image in /<type-folder>/<user-id>/img.png
        StorageReference ref = storageRef.child(rootFolder).child(String.valueOf(HaprampPreferenceManager.getInstance().getUserId()));
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
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        l(" uploaded to : " + downloadUrl.toString());
                        uploadPost(jobId, downloadUrl.toString());
                    }
                });

            }
        });


    }

    private void uploadPost(String jobId, String uploadedMediaUri) {

        l("Uploading Post");

        sendStatusBroadcast("Uploading: "+postJob.content);

        String media_url = uploadedMediaUri != null ? uploadedMediaUri.toString() : "";

        PostCreateBody body = new PostCreateBody(
                postJob.content,
                media_url,
                postJob.post_type,
                postJob.skills,
                1);

        DataServer.createPost(jobId, body, this);

    }

    @Override
    public void onDestroy() {

        // last round checkup
        PostCreationController.startDispatch();
        HaprampPreferenceManager.getInstance().setPostingServiceRunning(false);
        super.onDestroy();
    }

    private void sendStatusBroadcast(String msg){

        Intent intent = new Intent(Constants.ACTION_POST_UPLOAD);
        intent.putExtra("type",Constants.BROADCAST_TYPE_STATUS);
        intent.putExtra("msg",msg);
        sendBroadcast(intent);

    }

    private void sendFinishBroadcast(){

        Intent intent = new Intent(Constants.ACTION_POST_UPLOAD);
        intent.putExtra("type",Constants.BROADCAST_TYPE_FINISHED);
        sendBroadcast(intent);

    }

    private void sendErrorBroadcast(){

        Intent intent = new Intent(Constants.ACTION_POST_UPLOAD);
        intent.putExtra("type",Constants.BROADCAST_TYPE_ERROR);
        sendBroadcast(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPostCreated(String jobId) {

        sendFinishBroadcast();
        databaseHelper.removeJob(String.valueOf(jobId));
        l( "Job finished: " + jobId);

    }

    @Override
    public void onPostCreateError(String jobId) {

        sendErrorBroadcast();
       databaseHelper.setJobStatus(jobId,PostJobModel.JOB_PENDING);
        l( "Job failed : " + jobId);

    }

    private static void l(String msg){

        Log.d("SMSX",msg);

    }

}
