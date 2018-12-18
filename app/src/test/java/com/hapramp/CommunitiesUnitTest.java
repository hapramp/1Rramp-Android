package com.hapramp;

import com.hapramp.steemconnect4j.RpcJsonUtil;
import com.hapramp.steemconnect4j.StringUtils;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.utils.PostHashTagPreprocessor;
import com.hapramp.utils.PrizeMoneyFilter;
import com.hapramp.utils.RegexUtils;
import com.hapramp.utils.VoteUtils;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    assertTrue(HashTagUtils.isValidHashTag(tag2));
    assertFalse(HashTagUtils.isValidHashTag(tag3));
    assertTrue(HashTagUtils.isValidHashTag(tag4));
    assertFalse(HashTagUtils.isValidHashTag(tag5));
    assertTrue(HashTagUtils.isValidHashTag(tag6));
    assertFalse(HashTagUtils.isValidHashTag(tag7));
    assertFalse(HashTagUtils.isValidHashTag(tag8));
  }

  @Test
  public void testHashTagReFormatting() {
    String tag1 = "Nice";
    String tag2 = "#done";
    String tag3 = "###ForMyWork";
    String tag4 = "#####CruelDay";
    System.out.println(HashTagUtils.reformatHashTag(tag1));
    assertEquals("nice", HashTagUtils.reformatHashTag(tag1));

    System.out.println(HashTagUtils.reformatHashTag(tag2));
    assertEquals("done", HashTagUtils.reformatHashTag(tag2));

    System.out.println(HashTagUtils.reformatHashTag(tag3));
    assertEquals("formywork", HashTagUtils.reformatHashTag(tag3));

    System.out.println(HashTagUtils.reformatHashTag(tag4));
    assertEquals("cruelday", HashTagUtils.reformatHashTag(tag4));
  }

  @Test
  public void testHashTagProcessing() {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("hapramp-art");
    tags.add("art");
    tags.add("art");
    tags.add("art");
    tags.add("dance");
    tags.add("dance");
    tags.add("hapramp");

    ArrayList<String> expectedList = new ArrayList<>();
    expectedList.add("hapramp");
    expectedList.add("art");
    expectedList.add("hapramp-art");
    expectedList.add("dance");
    assertEquals(expectedList, PostHashTagPreprocessor.processHashtags(tags));
  }

  @Test
  public void testString() {
    ArrayList<Benf> benf = new ArrayList<>();
    benf.add(new Benf("bxute", 10000));
    benf.add(new Benf("bxute", 10000));
    System.out.println(benf.toString());
  }

  @Test
  public void testReblog() {
    String account = "bxute";
    String author = "vikonomics";
    String permlink = "what-dramatics-taught-me-about-handling-complexities";

    String params = StringUtils.getCommanSeparatedObjectString(
      RpcJsonUtil.getKeyValuePair("required_auths", "[]"),
      RpcJsonUtil.getKeyValuePair("required_posting_auths", "[\"bxute\"]"),
      RpcJsonUtil.getKeyValuePair("id", "\"follow\""),
      RpcJsonUtil.getKeyValuePair("json",
        "\"" + com.hapramp.utils.StringUtils.stringify(
          StringUtils.getCommanSeparatedArrayString(
            "\"reblog\"",
            "{\"account\":\"" + account + "\",\"author\":\"" + author + "\",\"permlink\":\"" + permlink + "\"}")) + "\""
      ));

    String operation = StringUtils.getOperationsString(
      StringUtils.getCommanSeparatedArrayString(
        StringUtils.getCommanSeparatedArrayString("\"custom_json\"", params)));

    System.out.println(operation);
  }

  @Test
  public void testVotingTransformation() {
    Map<Integer, Integer> voteVsRateMap = new HashMap<>();
    voteVsRateMap.put(1200, 1);
    voteVsRateMap.put(2200, 2);
    voteVsRateMap.put(3500, 2);
    voteVsRateMap.put(4000, 2);
    voteVsRateMap.put(4500, 3);
    voteVsRateMap.put(6000, 3);
    voteVsRateMap.put(7800, 4);
    voteVsRateMap.put(8000, 4);
    voteVsRateMap.put(8900, 5);
    for (Map.Entry<Integer, Integer> entry : voteVsRateMap.entrySet()) {
      int expectedRate = entry.getValue();
      int vote = VoteUtils.transformToRate(entry.getKey());
      assertEquals(expectedRate,vote);
    }
  }

  class Benf {
    private String author;
    private int weight;

    public Benf(String author, int weight) {
      this.author = author;
      this.weight = weight;
    }

    @Override
    public String toString() {
      return "{" +
        "\"author\":\"" + author + '\"' +
        ", \"weight\":" + weight +
        '}';
    }
  }
}
