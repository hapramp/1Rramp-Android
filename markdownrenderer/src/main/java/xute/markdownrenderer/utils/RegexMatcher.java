package xute.markdownrenderer.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher {
  public static String getMatch(String target, String regex, int groupIndex) {
    target = target.trim();
    Pattern r = Pattern.compile(regex);
    Matcher m = r.matcher(target);
    while (m.find()) {
      return m.group(groupIndex);
    }
    return "";
  }

  public static String getMatch(String target, String regex) {
    target = target.trim();
    Pattern r = Pattern.compile(regex);
    Matcher m = r.matcher(target);
    while (m.find()) {
      return m.group();
    }
    return "";
  }

  public static Matcher getMatcher(String target, String regex) {
    target = target.trim();
    Pattern r = Pattern.compile(regex);
    return r.matcher(target);
  }

}
