package bxute.logger;
import android.util.Log;

/**
 * Created by Ankit on 5/13/2017.
 */

public class L {


    public static class E {

        public static void m(String tag, String msg) {
                Log.e(tag, msg);
        }

    }

    public static class D {

        public static void m(String tag, String msg) {
                Log.d(tag, msg);
        }

    }

}
