package com.hapramp.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashTagUtils {
  public static boolean isValidHashTag(String text) {
    text = text + " ";
    Pattern pattern = Pattern.compile("#([a-zA-Z\\-]+)[ |\\n]");
    Matcher matcher = pattern.matcher(text);
    String matched = null;
    while (matcher.find()) {
      matched = matcher.group(1);
    }
    return (matched != null);
  }

  public static ArrayList<String> getHashTags(String body) {
    ArrayList<String> tags = new ArrayList<>();
    Pattern pattern = Pattern.compile("(^| |\\n|>)#([A-Za-z\\-]+\\b)(?!;)");
    Matcher matcher = pattern.matcher(body);
    while (matcher.find()) {
      tags.add(matcher.group(2).toLowerCase());
    }
    return tags;
  }

  public static String reformatHashTag(String text) {
    text = "#" + text;
    Pattern pattern = Pattern.compile("#([a-zA-Z\\-]+)[ |\\n]");
    Matcher matcher = pattern.matcher(text + " ");
    String matched = null;
    while (matcher.find()) {
      matched = matcher.group(1);
    }
    return matched.toLowerCase();
  }

  public static String cleanHashTagsFromBody(String body) {
    String hastag_pattern = "(^| |\\n|>)#([A-Za-z\\-]+\\b)(?!;)";
    return body.replaceAll(hastag_pattern, "");
  }
}
