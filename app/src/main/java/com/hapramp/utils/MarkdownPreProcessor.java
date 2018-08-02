package com.hapramp.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownPreProcessor {
  public static final String url_regex = "((http|https):\\/\\/.*?)[ |<|\"|)|\\\\|]";
  public static final String md_image_string_regex = "!((\\[.*?\\])(.*?)[)])";
  public static final String md_link_string_regex = "((\\[.*?\\])(.*?)[)])";
  //link text in group: 1
  //url in group: 2
  public static final String md_link_string_regex_2 = "\\[(.*?)\\](\\(.*?\\))";

  public String getCleanContent(String content) {
    content = removeNewlineChar(content);
    content = removeDirectLinks(content);
    content = removeMarkdownImageStrings(content);
    content = removeMarkdownStringLinksInPreservativeManner(content);
    content = removeHtmlTags(content);
    content = removeMarkdonwElements(content);
    content = removeExtraSpace(content);
    return content.trim();
  }

  private String removeNewlineChar(String content) {
    return content.replace("\n", " ");
    // return content.replaceAll(newLineCharRegex, " ");
  }

  private String removeDirectLinks(String content) {
    ArrayList<String> direcLinks = getListOfRegexMatches(content, url_regex, 1);
    for (int i = 0; i < direcLinks.size(); i++) {
      content = content.replace(direcLinks.get(i), "");
    }
    return content;
  }

  private String removeMarkdownImageStrings(String content) {
    return content.replaceAll(md_image_string_regex, "");
  }

  private String removeMarkdownStringLinksInPreservativeManner(String content) {
    //anchor text : group 1
    //url part: group 2
    String anchorText;
    Pattern r = Pattern.compile(md_link_string_regex_2);
    Matcher m = r.matcher(content);
    while (m.find()) {
      anchorText = m.group(1);
      content = content.replace(m.group(), anchorText);
    }
    return content;
  }

  private String removeHtmlTags(String content) {
    return content.replaceAll("(<.*?>)", " ");
  }

  private String removeMarkdonwElements(String content) {
    return content.replaceAll("#", "").replaceAll("\\*","");
  }

  private String removeExtraSpace(String content) {
    return content.replaceAll("[ ]{2,}", " ");
  }

  private ArrayList<String> getListOfRegexMatches(String content, String regex, int group) {
    ArrayList<String> links = new ArrayList<>();
    Pattern r = Pattern.compile(regex);
    Matcher m = r.matcher(content);
    while (m.find()) {
      links.add(m.group(group));
    }
    return links;
  }

  private String removeMarkdownStringLinks(String content) {
    return content.replaceAll(md_link_string_regex, "");
  }

  public String getFirstImageUrl(String content) {
    content = removeNewlineChar(content);
    ArrayList<String> allUrls = getAllImageLinks(content);
    for (int i = 0; i < allUrls.size(); i++) {
      if (isImageUrl(allUrls.get(i).toLowerCase())) {
        return allUrls.get(i);
      }
    }
    return "";
  }

  private ArrayList<String> getAllImageLinks(String body) {
    return getListOfRegexMatches(body, url_regex, 1);
  }

  private boolean isImageUrl(String url) {
    return url.contains(".png") || url.contains(".jpg") || url.contains(".jpeg");
  }
}
