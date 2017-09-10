package bxute.config;

/**
 * Created by Ankit on 7/21/2017.
 *
 * Responsible to providing system time in format
 *
 */

public class HaprampTime {

    private static HaprampTime mInstance;

    public HaprampTime() {
    }

    public static HaprampTime getInstance() {
        if (mInstance == null) {
            mInstance = new HaprampTime();
        }
        return mInstance;
    }
    public String getTime(){
        return "--time--";
    }
}
