package bxute.config;

import android.os.SystemClock;

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
}
