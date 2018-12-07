package com.hapramp.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
  private static Pattern pattern;
  private static Matcher matcher;

  public static String replaceMarkdownImage(String body) {
    return body.replaceAll("!\\[(.*?)\\]\\((.*?)[)]", "<img alt=\"$1\" src=\"$2\"/>");
  }

  public static String replacePlainImageLinks(String body) {
    pattern = Pattern.compile("(^|[^\"])((http(s|):.*?)(.png|.jpeg|.PNG|.gif|.jpg)(.*?))( |$|\\n|<)");
    matcher = pattern.matcher(body);
    while (matcher.find() && matcher.group(5).length() > 0) {
      body = new StringBuilder(body).replace(matcher.start(2), matcher.end(2),
        "<img src=\"" + matcher.group(2) + "\"/>").toString();
    }
    return body;
  }

  private static String replaceMarkdownLinks(String body) {
    return body.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "<a href=\"$2\">$1</a>");
  }
}
