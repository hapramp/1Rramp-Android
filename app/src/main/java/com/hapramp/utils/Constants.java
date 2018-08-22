package com.hapramp.utils;

/**
 * Created by Ankit on 5/25/2017.
 */

public class Constants {

  public static final String EXTRA_USERNAME = "username";
  public static final String EXTRA_ACCESS_TOKEN = "access_token";
  public static final String EXTRA_LOGIN_URL = "login_url";
  public static final int FEED_LOADING_LIMIT = 50;
  public static final String REGEX_URL = "(http:\\\\/\\\\/www\\\\.|https:\\\\/\\\\/www\" +\n" +
    "          \"\\\\.|http:\\\\/\\\\/|https:\\\\/\\\\/)?[a-z0-9]+([\\\\-\\\\.]{1}[a-z0-9]+)*\\\\.[a-z]{2,5}(:[0-9]\" +\n" +
    "          \"{1,5})?(\\\\/.*)?";
  public static final String EXTRAA_KEY_STEEM_USER_NAME = "steemUsername";
  public static final String EXTRAA_KEY_POST_ID = "postId";
  public static final boolean DEBUG = true;
  public static final String ACTION_NOTIFICATION_UPDATE = "comp.hapramp.notification_update";
  public static final String EXTRAA_KEY_POST_DATA = "postData";
  public static final String EXTRAA_KEY_COMMENTS = "comments_parcel";
  public static final java.lang.String EXTRAA_KEY_POST_AUTHOR = "postAuthor";
  public static final java.lang.String EXTRAA_KEY_POST_PERMLINK = "post_permlink";
}
