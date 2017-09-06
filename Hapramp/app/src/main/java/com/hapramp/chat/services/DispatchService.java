package com.hapramp.chat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hapramp.Constants;
import com.hapramp.chat.config.HaprampTime;
import com.hapramp.chat.config.MessageStatus;
import com.hapramp.chat.database.DatabaseHelper;
import com.hapramp.chat.models.JobModel;
import com.hapramp.chat.models.Message;
import com.hapramp.interfaces.MessageDispatchCallback;
import com.hapramp.interfaces.ReceivingConfirmationCallback;
import com.hapramp.interfaces.SeenConfirmationCallback;
import com.hapramp.logger.L;

import java.util.ArrayList;

/**
 * Created by Ankit on 7/21/2017.
 */

public class DispatchService extends Service implements MessageDispatchCallback, ReceivingConfirmationCallback, SeenConfirmationCallback {

    ArrayList<JobModel> jobs;
    private final String TAG = DispatchService.class.getSimpleName();
    private DatabaseHelper databaseHelper;
    private HaprampTime time;

    @Override
    public void onCreate() {
        super.onCreate();
        jobs = new ArrayList<>();
        databaseHelper = DatabaseHelper.getInstance();
        time = HaprampTime.getInstance();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {

            case Constants.ACTIONS.NEW_OUTGOING_MESSAGE:
                L.D.m(TAG, "new outgoing message job received");
                // get the new message and add to job list
                jobs.add(new JobModel(
                        JobModel.JOB_TYPE_MESSAGE,
                        intent.getStringExtra("msgId")
                ));

                break;

            case Constants.ACTIONS.NEW_MESSAGE_RECEIVED:
                L.D.m(TAG, "new message delivery job received");
                // get the new message and add to job list
                jobs.add(new JobModel(
                        JobModel.JOB_TYPE_RECEIVING,
                        intent.getStringExtra("msgId")
                ));

                break;

            case Constants.ACTIONS.MESSAGE_SEEN:
                L.D.m(TAG, "new message seen job received");
                // get the new message and add to job list
                jobs.add(new JobModel(
                        JobModel.JOB_TYPE_SEEN,
                        intent.getStringExtra("msgId")
                ));

                break;

            default:
                L.D.m(TAG, "start complete job afresh");
                refreshJobsAndDispatch();
        }
        return START_NOT_STICKY;

    }

    private void refreshJobsAndDispatch() {

        jobs = DatabaseHelper.getInstance().getJobs();
        dispatch(getSameJob());

    }

    private void dispatch(JobModel job) {
        // TODO: 7/21/2017 implement the retrofit network call
    }

    private JobModel getNextJob() {
        removeJob();
        return jobs.get(0);
    }

    private JobModel getSameJob() {
        return jobs.get(0);
    }

    private void removeJob() {
        jobs.remove(0);
    }

    @Override
    public void onMessageDispatched() {

        Message messageToUpdate = databaseHelper.getMessage(getSameJob().msgId);
        messageToUpdate.setSent_time(time.getTime());
        messageToUpdate.setStatus(MessageStatus.STATUS_SENT);
        databaseHelper.updateMessage(messageToUpdate);
        // dispatch next
        dispatch(getNextJob());

    }

    @Override
    public void onMessageDispatchFailed() {
        dispatch(getSameJob());
    }

    @Override
    public void onSeenConfirmed() {

        Message messageToUpdate = databaseHelper.getMessage(getSameJob().msgId);
        messageToUpdate.setSeen_time(time.getTime());
        messageToUpdate.setStatus(MessageStatus.STATUS_SEEN_CONFIRMED);
        databaseHelper.updateMessage(messageToUpdate);
        // dispatch next
        dispatch(getNextJob());
    }

    @Override
    public void onSeenConfirmationFailed() {
        dispatch(getSameJob());
    }

    @Override
    public void onReceivingConfirmed() {

        Message messageToUpdate = databaseHelper.getMessage(getSameJob().msgId);
        messageToUpdate.setDelivered_time(time.getTime());
        messageToUpdate.setStatus(MessageStatus.STATUS_DELIVERY_CONFIRMED);
        databaseHelper.updateMessage(messageToUpdate);
        // dispatch next
        dispatch(getNextJob());

    }

    @Override
    public void onReceivingConfirmationFailed() {
        dispatch(getSameJob());
    }

}
