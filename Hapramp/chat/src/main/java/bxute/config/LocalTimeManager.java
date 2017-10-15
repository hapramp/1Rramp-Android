package bxute.config;

import android.os.SystemClock;

import java.util.Calendar;

/**
 * Created by Ankit on 7/21/2017.
 *
 * Responsible to providing system time in format
 *
 */

public class LocalTimeManager {

    private static LocalTimeManager mInstance;

    public LocalTimeManager() {
    }

    public static LocalTimeManager getInstance() {
        if (mInstance == null) {
            mInstance = new LocalTimeManager();
        }
        return mInstance;
    }
    public String getTime(){
        return String.valueOf(SystemClock.currentThreadTimeMillis());
    }

    public String getDateTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + ":" + (calendar.get(Calendar.MONTH)) + ":" + calendar.get(Calendar.DAY_OF_MONTH) + ":" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)+":"+calendar.get(Calendar.AM_PM);
    }
}
