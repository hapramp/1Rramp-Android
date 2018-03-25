package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.hapramp.preferences.HaprampPreferenceManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by Ankit on 12/23/2017.
 */

public class PostJobModel implements Parcelable {

    private static final int MAX_LENGTH = 64;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({JOB_SUCCEEDED,JOB_PENDING})
    public @interface JobStatus{}
    public static final int JOB_SUCCEEDED = 1;
    public static final int JOB_PENDING = 2;

    public int jobStatus;
    public String content;
    public String media_uri;
    public int post_type;
    public List<Integer> skills;
    public int contest_id;
    public String jobId;

    public PostJobModel(String jobId, String content, String media_uri, int post_type, List<Integer> skills, int contest_id ,@JobStatus int jobStatus) {

        this.jobId = jobId;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.skills = skills;
        this.contest_id = contest_id;
        this.jobStatus = jobStatus;

    }



    public PostJobModel(String jobId, String content, String media_uri, int post_type, String skills, int contest_id,@JobStatus int jobStatus) {

        this.jobId = jobId;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.skills = tokenizeSkills(skills);
        this.contest_id = contest_id;
        this.jobStatus = jobStatus;
    }

    private List<Integer> tokenizeSkills(String s){

        StringTokenizer tokenizer = new StringTokenizer(s,"#");
        List<Integer> skills = new ArrayList<>();

        for (int i=0;i<tokenizer.countTokens();i++){
            skills.add(Integer.valueOf(tokenizer.nextToken("#")));
        }
        return skills;

    }

    public String getSkillsAsPaddedString(){

        StringBuilder builder = new StringBuilder();

        for (int i=0;i<skills.size();i++){
            builder.append(skills.get(i))
                    .append("#");
        }

        return builder.toString();

    }

    protected PostJobModel(Parcel in) {
        content = in.readString();
        media_uri = in.readString();
        post_type = in.readInt();
        if (in.readByte() == 0x01) {
            skills = new ArrayList<Integer>();
            in.readList(skills, Integer.class.getClassLoader());
        } else {
            skills = null;
        }
        contest_id = in.readInt();
        jobId = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(media_uri);
        dest.writeInt(post_type);
        if (skills == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(skills);
        }
        dest.writeInt(contest_id);
        dest.writeString(jobId);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PostJobModel> CREATOR = new Parcelable.Creator<PostJobModel>() {
        @Override
        public PostJobModel createFromParcel(Parcel in) {
            return new PostJobModel(in);
        }

        @Override
        public PostJobModel[] newArray(int size) {
            return new PostJobModel[size];
        }
    };

    @Override
    public String toString() {

        return "PostJobModel{" +
                "content='" + content + '\'' +
                ", media_uri='" + media_uri + '\'' +
                ", post_type=" + post_type +
                ", skills=" + skills +
                ", contest_id=" + contest_id +
                '}';

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            return dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getMediaLocation(){

        String user_id = HaprampPreferenceManager.getInstance().getUserId();

        StringBuilder builder = new StringBuilder()
                .append(user_id)
                .append("_")
                .append(getCurrentTimeStamp())
                .append("_")
                .append(random());

        return builder.toString();

    }

}
