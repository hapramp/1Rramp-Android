package com.hapramp;

import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
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
 * Example local unit test, which will execute on the development machine (host).1514160723000
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {

        String time = "";
        String givenDateString = "2017-12-27-04:41:34.925147";
        givenDateString = givenDateString.replace('T','-');
        givenDateString = "GMT+06:30 ".concat(givenDateString);
        SimpleDateFormat sdf = new SimpleDateFormat("z yyyy-MM-dd-HH:mm:ss");
        try {

            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }

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