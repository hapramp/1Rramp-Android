package xute.markdownrenderer.utils;

public class ItemSeparator {
  private static String realDataMatcher = "(\\n){1,}";
  private static String testDataMatcher = "(\\\\n){1,}";

  public static String[] getListOfItems(String body) {
    return body.split(realDataMatcher);
  }
}
