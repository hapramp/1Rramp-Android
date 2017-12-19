package com.hapramp;

import android.net.Uri;
import android.util.Log;

import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {

        String url = " https://firebasestorage.googleapis.com/v0/b/hapramp-625c8.appspot.com/o/userProfile/40/1510857552637_/IMG_20171029_152706.jpg?alt=media&token=1f560674-8471-4f68-a9da-99552a5a3406";
        getImageUrl(url);
    }

    private void getImageUrl(String url) {

        String s1 = url.substring(0,url.lastIndexOf('/')+1);
        String s2 = url.substring(url.lastIndexOf('/')+1);
        p("First Part "+s1);
        p("Second Part "+s2);

    }

    private void p(String s) {
        System.out.println(s);
    }

}