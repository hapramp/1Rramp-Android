package com.hapramp.utils;

import android.content.Context;
import android.content.Intent;

import com.hapramp.steem.models.Feed;

/**
 * Created by Ankit on 5/9/2018.
 */

public class ShareUtils {
  public static void share(Context context, String content) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, content);
    sendIntent.setType("text/plain");
    context.startActivity(sendIntent);
  }

  public static void shareMixedContent(Context context, Feed feed) {

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_TEXT, getFormattedTextForSharing(feed.getTitle(),
      feed.getCleanedBody(), feed.getAuthor(), feed.getPermlink()));
      shareIntent.setType("text/plain");
    context.startActivity(shareIntent);
  }

  private static String getFormattedTextForSharing(String title, String text, String username, String permlink) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(title);
    stringBuilder.append("\n");
    int len = (text.length() > 140 ? 140 : text.length());
    stringBuilder.append(text.substring(0, len));
    stringBuilder.append("...\n");
    stringBuilder.append("https://alpha.hapramp.com/");
    stringBuilder.append("@").append(username).append("/").append(permlink);
    stringBuilder.append("\n\nHapRamp, The social media platform for creative artists");
    return stringBuilder.toString();
  }


}
