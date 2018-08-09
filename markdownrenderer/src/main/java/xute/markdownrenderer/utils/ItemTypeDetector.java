package xute.markdownrenderer.utils;

import android.util.Log;

import java.util.regex.Matcher;

import xute.markdownrenderer.ItemModel;
import xute.markdownrenderer.ItemType;

public class ItemTypeDetector {

  public static ItemModel getType(String item) {
    Log.d("RendererView", "Parsing " + item);
    ItemModel itemModel = new ItemModel();
    String match = RegexMatcher.getMatch(item, "^# (.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HEADING_ONE);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^## (.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HEADING_TWO);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^### (.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HEADING_THREE);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^#### (.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HEADING_FOUR);
      itemModel.setContent(match);
      return itemModel;
    }


    match = RegexMatcher.getMatch(item, "^##### (.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HEADING_FIVE);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "---", 0);
    if (match.length() > 0) {
      itemModel.setType(ItemType.HORIZONTAL_LINE);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^-(.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.UL_BULLET);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^([0-9].)(.*)");
    if (match.length() > 0) {
      itemModel.setType(ItemType.OL_BULLET);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "^>(.*)", 1);
    if (match.length() > 0) {
      itemModel.setType(ItemType.BLOCKQUOTE);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "!\\[(.*?)\\]\\((.*?)[)]", 2);
    if (match.length() > 0) {
      itemModel.setType(ItemType.IMAGE);
      itemModel.setContent(match);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", 0);
    if (match.length() > 0 && isImageUrl(match)) {
      itemModel.setType(ItemType.IMAGE);
      Log.d("Renderer","Image match "+match);
      itemModel.setContent(match);
      return itemModel;
    }

    Matcher matcher = RegexMatcher.getMatcher(item, "<a href=['|\"](.*?)['|\"]>(.*?)<img src=['|\"](.*?)['|\"]");
    String imageUrl = "";
    String linkUrl = "";
    match = "";
    while (matcher.find()) {
      match = matcher.group();
      imageUrl = matcher.group(3);
      linkUrl = matcher.group(1);
    }
    if (match.length() > 0) {
      itemModel.setType(ItemType.IMAGE);
      itemModel.setContent(imageUrl);
      itemModel.setExtra(linkUrl);
     // Log.d("RendererView", "matched image link to " + linkUrl);
      return itemModel;
    }

    match = RegexMatcher.getMatch(item, "<img src=[\"|'](.*?)[\"|']", 1);
    if (match.length() > 0 && isImageUrl(match)) {
      itemModel.setType(ItemType.IMAGE);
      itemModel.setContent(match);
      return itemModel;
    }

    itemModel.setType(ItemType.NORMAL_TEXT);
    itemModel.setContent(item);
    return itemModel;
  }

  private static boolean isImageUrl(String url) {
    return url.contains(".png") || url.contains(".jpg") || url.contains(".jpeg");
  }
}
