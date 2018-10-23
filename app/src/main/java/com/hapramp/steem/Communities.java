package com.hapramp.steem;

public class Communities {
  public static final String FEEDS = "feeds";
  public static final String EXPLORE = "explore";
  public static final String TAG_HAPRAMP = "hapramp";
  public static final String[] supportedCommunities = {
    "art",
    "dance",
    "travel",
    "literature",
    "film",
    "photography",
    "fashion",
    "design"};

  public static boolean doesCommunityExists(String community_tag) {
    for (int i = 0; i < supportedCommunities.length; i++) {
      if (community_tag.toLowerCase().equals(supportedCommunities[i])) {
        return true;
      }
    }
    return false;
  }
}
