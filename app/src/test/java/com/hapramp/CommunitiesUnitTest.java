package com.hapramp;

import com.hapramp.datastore.SteemRequestBody;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.utils.RegexUtils;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
    String string1 = "https://github.com/#trending. #Blockchain is a great #tech." +
      " <h1>#Crypto-world</h1> will grow in #future.";
    ArrayList<String> exp = new ArrayList<>();
    exp.add("blockchain");
    exp.add("tech");
    exp.add("crypto-world");
    exp.add("future");
    ArrayList<String> reality = HashTagUtils.getHashTags(string1);
    for (int i = 0; i < reality.size(); i++) {
      assertEquals(exp.get(i), reality.get(i));
    }
  }

  @Test
  public void testHashTagValidator() {
    String tag1 = "#nice";
    String tag2 = "#B4U";
    String tag3 = "#Random$Char";
    String tag4 = "#Human-work";
    String tag5 = "#15Day";
    String tag6 = "#ForMyWork";
    String tag7 = "#Syntax{}";
    String tag8 = "#Hello%2";
    assertTrue(HashTagUtils.isValidHashTag(tag1));
    assertFalse(HashTagUtils.isValidHashTag(tag2));
    assertFalse(HashTagUtils.isValidHashTag(tag3));
    assertTrue(HashTagUtils.isValidHashTag(tag4));
    assertFalse(HashTagUtils.isValidHashTag(tag5));
    assertTrue(HashTagUtils.isValidHashTag(tag6));
    assertFalse(HashTagUtils.isValidHashTag(tag7));
    assertFalse(HashTagUtils.isValidHashTag(tag8));
  }

  @Test
  public void testHashTagReFormatting(){
    String tag1 = "Nice";
    String tag2 = "#done";
    String tag3 = "###ForMyWork";
    String tag4 = "#####CruelDay";
    System.out.println(HashTagUtils.reformatHashTag(tag1));
    assertEquals("nice",HashTagUtils.reformatHashTag(tag1));

    System.out.println(HashTagUtils.reformatHashTag(tag2));
    assertEquals("done",HashTagUtils.reformatHashTag(tag2));

    System.out.println(HashTagUtils.reformatHashTag(tag3));
    assertEquals("formywork",HashTagUtils.reformatHashTag(tag3));

    System.out.println(HashTagUtils.reformatHashTag(tag4));
    assertEquals("cruelday",HashTagUtils.reformatHashTag(tag4));

  }
}
