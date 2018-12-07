package com.hapramp.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
  private static Pattern pattern;
  private static Matcher matcher;
  public static String getHtmlContent(String md) {
    try {
      Parser parser = Parser.builder().build();
      Node document = parser.parse(md);
      HtmlRenderer renderer = HtmlRenderer.builder().build();
      String text = renderer.render(document);
      text = RegexUtils.replaceMarkdownImage(text);
      text = RegexUtils.replacePlainImageLinks(text);
      text = RegexUtils.replaceMarkdownLinks(text);
      return text;
    }catch (Exception e){
      e.printStackTrace();
    }
    return md;
  }

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
