package com.hapramp.logger;
import android.util.Log;

import com.hapramp.Constants;

/**
 * Created by Ankit on 5/13/2017.
 */

public class L {


    public static class E {

        public static void m(String tag, String msg) {
            if (Constants.DEBUG)
                Log.e(tag, msg);
        }

    }

    public static class D {

        public static void m(String tag, String msg) {
            if (Constants.DEBUG)
                Log.d("__DEBUG__ "+tag, msg);
        }

    }

}
