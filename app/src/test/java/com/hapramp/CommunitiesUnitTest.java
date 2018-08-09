package com.hapramp;

import org.junit.Test;

import xute.markdownrenderer.utils.MarkdownRendererUtils;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {
  @Test
  public void testMd(){
    String body = "<center><a href='https://d.tube/#!/v/kylekulinski/sk0naycc'><img src='https://ipfs.io/ipfs/Qmf6ySeFie7k34hgCWdNtkPZCwDHnX2q89XqcTQe2gBwzn'></a></center><hr>\\n\\nSupport The Show On Patreon:\\nhttps://www.patreon.com/seculartalk\\n\\nHere's Our Amazon Link:\\nhttps://www.amazon.com/?tag=seculacom-20\\n\\nFollow Kyle on Twitter:\\nhttp://www.twitter.com/kylekulinski\\n\\nLike the show on Facebook:\\nhttp://www.facebook.com/SecularTalk\\n\\nWatch this clip on YouTube:\\nhttps://www.youtube.com/watch?v=ARQ6YpZ_YEk\\n\\nClip from The Kyle Kulinski Show, which airs live on Blog Talk Radio and Secular Talk Radio Monday - Friday 11:00 AM - 12:30 PM Eastern time zone.\\n\\nListen to the Live Show or On Demand archive at:\\nhttp://www.blogtalkradio.com/kylekuli...\\n\\n\\nCheck out our website - and become a member - at:\\nhttp://www.SecularTalkRadio.com\\n\\n<hr><a href='https://d.tube/#!/v/kylekulinski/sk0naycc'> ▶️ DTube</a><br /><a href='https://ipfs.io/ipfs/Qmb8vBjfKhqXfb7WWPdYbVHPcSrri7V5CMHxUWVosi74tu'> ▶️ IPFS</a>";
    String result = MarkdownRendererUtils.replaceYoutubeWithLinkImage(body);
    System.out.println(result);
  }
}
