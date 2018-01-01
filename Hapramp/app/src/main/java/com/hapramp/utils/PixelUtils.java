package com.hapramp.utils;

import android.content.res.Resources;

/**
 * Created by Ankit on 1/1/2018.
 */

public class PixelUtils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
