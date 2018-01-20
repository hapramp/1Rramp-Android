package com.hapramp.services;

import android.os.Build;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;

/**
 * Created by Ankit on 1/20/2018.
 */

public class JobManager {

    public static void init() {

        Log.d("JobManager","Scheduling Job");

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(HapRampMain.getContext()));
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(PostService.class)
                // uniquely identifies the job
                .setTag(Constants.DISPATCHER_UNIQUE_ID)
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 5))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        dispatcher.mustSchedule(myJob);

    }

}
