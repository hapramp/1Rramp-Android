package com.hapramp;

import android.net.Uri;
import android.os.SystemClock;
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
import java.util.HashMap;
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

    }

    public void testHashMap() {

        HashMap<String, ArrayList<String>> filterMap = new HashMap<>();
        ArrayList<String> strings = filterMap.get("all") != null ? filterMap.get("all") : new ArrayList<String>();
        filterMap.put("all", strings);

    }


}