package com.hapramp;

import com.hapramp.utils.RegexUtils;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {
  @Test
  public void testImageLink() {
    String url1 = "https://some-website.com/some-image.jpg";
    String url2 = "<img src=\"https://some-website.com/some-image.png?s=200&v=4\"/>";
    String url3 = "<p>https://some-website.com/some-image.png?s=200&v=4</p>";
    String url4 = "Hello Developer.https://some-website.com/some-image.png?s=200&v=4";

    assertEquals("<img src=\"https://some-website.com/some-image.jpg\"/>",
      RegexUtils.replacePlainImageLinks(url1));

    assertEquals("<img src=\"https://some-website.com/some-image.png?s=200&v=4\"/>",
      RegexUtils.replacePlainImageLinks(url2));

    assertEquals("<p><img src=\"https://some-website.com/some-image.png?s=200&v=4\"/></p>",
      RegexUtils.replacePlainImageLinks(url3));

    assertEquals("Hello Developer.<img src=\"https://some-website.com/some-image.png?s=200&v=4\"/>",
      RegexUtils.replacePlainImageLinks(url4));
  }

  @Test
  public void testHashTags() {
    String string1 = "https://github.com/#trending. #blockchain is a great #tech." +
      " <h1>#crypto-world</h1> will grow in #future.";
    ArrayList<String> exp = new ArrayList<>();
    exp.add("blockchain");
    exp.add("tech");
    exp.add("crypto-world");
    exp.add("future");
    ArrayList<String> reality = RegexUtils.getHashTags(string1);
    for (int i = 0; i < reality.size(); i++) {
      assertEquals(exp.get(i), reality.get(i));
    }
  }
}
