package bxute.models;

import android.support.annotation.IntDef;

/**
 * Created by Ankit on 7/21/2017.
 */

public class JobModel {

    public static final int JOB_TYPE_MESSAGE = 10;
    public static final int JOB_TYPE_RECEIVING = 11;
    public static final int JOB_TYPE_SEEN = 12;

    @IntDef({JOB_TYPE_MESSAGE,JOB_TYPE_RECEIVING,JOB_TYPE_SEEN})
    @interface JobType{}

    @JobModel.JobType
    public int jobType;
    public String msgId;

    public JobModel(@JobType int jobType, String msgId) {
        this.jobType = jobType;
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "JobModel{" +
                "jobType=" + jobType +
                ", msgId='" + msgId + '\'' +
                '}';
    }

}
