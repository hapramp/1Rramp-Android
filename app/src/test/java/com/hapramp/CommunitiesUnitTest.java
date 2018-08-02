package com.hapramp;

import com.hapramp.steem.models.data.JsonMetadata;
import com.hapramp.utils.MarkdownPreProcessor;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {

  public static final String PHOTOGRAPHY = "hapramp-photography";
  public static final String ART = "hapramp-art";
  public static final String MUSIC = "hapramp-music";
  public static final String FASHION = "hapramp-fashion";
  public static final String DANCE = "hapramp-dance";
  public static final String DRAMATICS = "hapramp-dramatics";
  public static final String TRAVEL = "hapramp-travel";
  public static final String LITERATURE = "hapramp-literature";
  public ArrayList<String> communities;
  public static final String body = "[![Ezira Hangout Banner.png](https://steemitimages.com/DQmVyfrVqdp82KnzGJAdDpFjNpSmPj41WQsWFQwVy7uRRVh/Ezira%20Hangout%20Banner.png)](www.ezira.io)\\n\\n# <center> [Ezira combines blockchain social media, with a decentralized exchange and a peer to peer marketplace.](https://www.ezira.io) </center> \\n\\n# <center> [Join the Ezira Discord channel](https://discord.gg/XhbGfrd) and tune in at 1:00PM UTC on Sunday 29th of July to take part in the next Ezira Hangout! Comment your question or topic, and it will be discussed live. </center>\\n\\n# <center> Build The Next World. </center>\\n<center> <iframe width=\\\"1903\\\" height=\\\"864\\\" src=\\\"https://www.youtube.com/embed/9NkurK6Stb4\\\" frameborder=\\\"0\\\" allow=\\\"autoplay; encrypted-media\\\" allowfullscreen></iframe> </center>\\n\\n### <center> [Watch the Ezira presentation at the Melbourne blockchain Center](https://www.facebook.com/blockchaincentre/videos/1788750667869563/?t=160)</center>\\n\\n### <center> Ezira is running a preliminary Initial Coin Offering on Bitshares, using the asset EZIRA. Each";

  @Before
  public void populateCommunityList() {
    communities = new ArrayList<>();
    communities.add(PHOTOGRAPHY);
    communities.add(ART);
    communities.add(MUSIC);
    communities.add(FASHION);
    communities.add(DANCE);
    communities.add(DRAMATICS);
    communities.add(TRAVEL);
    communities.add(LITERATURE);
  }

  @Test
  public void testImageLinks() {
    MarkdownPreProcessor markDownExtractor = new MarkdownPreProcessor();
    System.out.println("First image " + markDownExtractor.getFirstImageUrl(body));
  }

  @Test
  public void testCleanContent() {
    List<String> tags = new ArrayList<>();
    List<String> images = new ArrayList<>();
    tags.add("art");
    tags.add("bitcoin");
    images.add("http://www.ankit.new.png");
    images.add("http://www.ankit.old.png");
    JsonMetadata jsonMetadata = new JsonMetadata(tags, images);
    System.out.println(com.hapramp.utils.StringUtils.stringify(jsonMetadata.getJson()));
  }

}
