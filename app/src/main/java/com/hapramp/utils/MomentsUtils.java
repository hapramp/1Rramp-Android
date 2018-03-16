package com.hapramp.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Ankit on 12/15/2017.
 */
// ref: https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
public class MomentsUtils {

    //2018-03-08T11:38:48
    public static String getFormattedTime(String timeStamp){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        String formattedTime  = "";
        timeStamp = timeStamp.replace('T','-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        sdf.setTimeZone(tz);

        try {

            Date mDate = sdf.parse(timeStamp);
            long timeInMilliseconds = mDate.getTime();
            formattedTime = DateUtils.getRelativeTimeSpanString(timeInMilliseconds).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedTime;
    }

}
