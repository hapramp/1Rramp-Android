package com.hapramp.utils;

import android.util.Log;

/**
 * Created by Ankit on 12/17/2017.
 */

public class ImageUrlGenerator {

    private static final int IMAGE_TYPE_SMALL = 0;
    private static final int IMAGE_TYPE_MEDIUM = 1;
    private static final int IMAGE_TYPE_LARGE = 2;

    private static final String prefix = "thumb_";

    public static String getGeneratedUrl(String url){
        // todo: add support of multiple version
        return format(url);

    }

    private static String format(String url) {

        String s1 = url.substring(0,url.lastIndexOf('/')+1);
        String s2 = url.substring(url.lastIndexOf('/')+1);
        Log.d("URLF","new Url "+s1.concat("i_").concat(s2));
        return s1.concat(prefix).concat(s2);

    }

}


