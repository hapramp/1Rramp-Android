package com.hapramp;

import com.hapramp.utils.MarkdownRendererUtils;
import com.twitter.Extractor;

import org.junit.Test;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {
  @Test
  public void testMarkdown() {
    Extractor extractor = new Extractor();
    String text = "@bxute has http://www.gg.com/@nice";
    for (String hashtag : extractor.extractMentionedScreennames(text)) {
      System.out.println(hashtag);
    }
  }
}
