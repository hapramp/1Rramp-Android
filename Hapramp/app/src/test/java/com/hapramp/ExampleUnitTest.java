package com.hapramp;

import android.net.Uri;
import android.util.Log;

import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

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
        URL url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=98ff9a55378b9f07827a7dc0925d7d77");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        //scanner.useDelimiter("\\A");
        if(scanner.hasNext()){
            p(scanner.next());
        }else{
            p("Nothing");
        }

    }

    private void p(String s ){
        System.out.println(s);
    }
}