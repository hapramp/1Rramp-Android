package com.hapramp.utils;

import android.text.format.DateUtils;

import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ankit on 12/15/2017.
 */
// ref: https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
public class MomentsUtils {

    public static int TIME_DIFF_FOR_CREATING_POST = 300000;

    //2018-03-08T11:38:48
    public static String getFormattedTime(String timeStamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            long _time = dateFormat.parse(timeStamp).getTime();
            //minResolution: SECONDS_IN_MILLIS
            //transitionResolution: YEAR_IN_MILLIS
            return DateUtils.getRelativeDateTimeString(HapRampMain.getContext(), _time, DateUtils.MINUTE_IN_MILLIS, DateUtils.YEAR_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return dateFormat.format(calendar.getTime());
    }

    public static String getTimeLeft() {
        long deadline = HaprampPreferenceManager.getInstance().getLastPostCreatedAt() + TIME_DIFF_FOR_CREATING_POST;
        return getFormattedTime(getTimeFromMillis(deadline));
    }

    private static String getTimeFromMillis(long millisTime) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisTime);
        return formatter.format(calendar.getTime());

    }

    public static boolean isAllowedToCreatePost() {
        long lastCreatedAt = HaprampPreferenceManager.getInstance().getLastPostCreatedAt();
        long now = System.currentTimeMillis();
        return  (now - lastCreatedAt) > TIME_DIFF_FOR_CREATING_POST;
    }

}
