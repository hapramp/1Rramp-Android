package com.hapramp.steem;

public class Communities {
  public static final String FEEDS = "feeds";
  public static final String EXPLORE = "explore";
  public static final String TAG_HAPRAMP = "hapramp";
  public static final String ART = "hapramp-art";
  public static final String DANCE = "hapramp-dance";
  public static final String TRAVEL = "hapramp-travel";
  public static final String LITERATURE = "hapramp-literature";
  public static final String FILM = "hapramp-film";
  public static final String PHOTOGRAPHY = "hapramp-photography";
  public static final String FASHION = "hapramp-fashion";
  public static final String DESIGN = "hapramp-design";
  public static final String MUSIC = "hapramp-music";
  public static final String EDIT_BORDER = "edit-border";
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
