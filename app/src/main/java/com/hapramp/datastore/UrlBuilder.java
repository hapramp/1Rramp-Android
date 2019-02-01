package com.hapramp.datastore;

import android.content.Context;

import com.hapramp.R;
import com.hapramp.main.HapRampMain;

import java.util.Locale;

import static com.hapramp.api.URLS.BASE_URL;
import static com.hapramp.api.URLS.STEEMIT_API_URL;

public class UrlBuilder {
  private static final int FEED_LOADING_LIMIT = 50;

  public static String explorePostsUrl() {
    return BASE_URL +
      "feeds/all";
  }

  public static String competitionsListUrl() {
    return BASE_URL +
      "competitions";
  }

  public static String competitionsListUrlAfterId(String id) {
    return String.format("%scompetitions/~list?limit=4&start_after_id=%s", BASE_URL, id);
  }

  public static String rcInfoUrl(String username) {
    return BASE_URL +
      "rc/" + username;
  }

  public static String competitionEntryUrl(String comp_Id) {
    return BASE_URL + "competitions/" + comp_Id + "/posts";
  }

  public static String competitionWinnersUrl(String comp_Id) {
    return BASE_URL + "competitions/" + comp_Id + "/winners";
  }

  public static String steemUrl() {
    return STEEMIT_API_URL;
  }

  public static String userCommunityUrl(String username) {
    return BASE_URL +
      "users/usernames/" +
      username;
  }

  public static String judgesListUrl() {
    return BASE_URL +
      "judges";
  }

  public static String createCompetitionUrl() {
    return BASE_URL +
      "competitions";
  }

  public static String competitionEligibilityCheckUrl(String username) {
    return BASE_URL +
      "users/usernames/" +
      username;
  }

  public static String allCommunityUrl() {
    return BASE_URL + "communities";
  }

  public static String microCommunityPostsUrl(String tag, String order, int limit) {
    return String.format(Locale.US, "%smicro-communities/%s/posts/%s?limit=%d",
      BASE_URL, tag, order, limit);
  }

  public static String microCommunityPostsUrl(String tag, String order, int limit, String start_author,
                                              String start_permlink) {
    return String.format(Locale.US,
      "%smicro-communities/%s/posts/%s?limit=%d&start_author=%s&start_permlink=%s",
      BASE_URL, tag, order, limit, start_author, start_permlink);
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

  public static String userProfileUrl(String username) {
    return String.format("https://steemit.com/@%s.json", username);
  }

  public static String getUserProfileUrl(String username) {
    return String.format(HapRampMain.getContext().getString(R.string.steem_user_profile_pic_format), username);
  }
}
