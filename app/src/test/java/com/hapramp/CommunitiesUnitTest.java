package com.hapramp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {
  @Test
  public void testHash() {
    String con = "#Hello this is #important blog on #technology. I know this is for #2020-plan.";
    Pattern pattern = Pattern.compile("#(\\w+)");
    Matcher matcher = pattern.matcher(con);
    while (matcher.find()) {
      System.out.println(matcher.group(1));
    }
  }
}
