package com.hapramp.datastore;

public class URLS {
  private static final int FEED_LOADING_LIMIT = 50;
  private static final String BASE_URL = "https://api.hapramp.com/api/v2/";
  private static final String TEST_BASE_URL = "https://testapi.hapramp.com/api/v2/";
  private static final String STEEMIT_API_URL = "https://api.steemit.com";
  private static final String STEEMIT_URL = "https://steemit.com/";

  public static String explorePostsUrl() {
    return BASE_URL +
      "feeds/all";
  }

  public static String steemUrl() {
    return STEEMIT_API_URL;
  }

  public static String userCommunityUrl(String username) {
    return BASE_URL +
      "users/usernames/" +
      username;
  }

  // TODO: 23/10/18 remove test api
  public static String competitionEligibilityCheckUrl(String username) {
    return TEST_BASE_URL +
      "users/usernames/" +
      username;
  }

  public static String allCommunityUrl() {
    return BASE_URL + "communities";
  }

  public static String updateCommunitySelectionUrl() {
    return BASE_URL + "users/communities";
  }

  public static String uploadUrl() {
    return BASE_URL + "upload";
  }

  public static String curationUrl(String tag) {
    return BASE_URL + "curation/tag/" + tag;
  }

  public static String userBlogUrl(String username) {
    return BASE_URL + "feeds/blog/" + username + "?limit=8";
  }

  public static String userBlogUrl(String username, String start_author, String start_permlink) {
    return BASE_URL + "feeds/blog/" + username +
      "?limit=8" +
      "&start_author=" + start_author +
      "&start_permlink=" + start_permlink;
  }

  public static String userFeedUrl(String username) {
    return BASE_URL + "feeds/user/" + username + "?limit=" + FEED_LOADING_LIMIT;
  }

  public static String userFeedUrl(String username, final String start_author, final String start_permlink) {
    return BASE_URL + "feeds/user/" + username +
      "?limit=" + FEED_LOADING_LIMIT +
      "&start_author=" + start_author +
      "&start_permlink=" + start_permlink;
  }

  public static String userProfileUrl(String username){
    return String.format("https://steemit.com/@%s.json", username);
  }
}
