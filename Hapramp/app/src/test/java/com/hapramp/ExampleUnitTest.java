package com.hapramp;

import android.net.Uri;
import android.util.Log;

import org.junit.Test;

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
        String S = "aaaaa";
        int len  = S.length() - 1;
        int max = 0;
        String first = S.substring(0,len);
        String sec = S.substring(1);

        for(int i = len;i>=1;i--){
            int li = len - i;
            if(first.substring(0,i).equals(sec.substring(li))){
                max = i;
                break;
            }
        }

        p(String.valueOf(max));

    }

    private void p(String s ){
        System.out.println(s);
    }
}