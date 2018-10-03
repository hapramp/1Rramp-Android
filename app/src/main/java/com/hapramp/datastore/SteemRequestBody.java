package com.hapramp.datastore;

public class SteemRequestBody {

  public static String getResourceCreditBody(String username) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"rc_api.find_rc_accounts\"," +
      " \"params\":{\"accounts\": [\"" + username + "\"]}, \"id\":1}";
  }

  public static String getSinglePostBody(String author, String permlink) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_content\", \"params\":[\"" + author + "\"," +
      " \"" + permlink + "\"], \"id\":1}";
  }

  /*
   * For: New in 1ramp section [Posts with tag hapramp tag]
   * */
  public static String discussionsByCreated(String tag, int limit) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\"," +
      " \"params\":[{\"tag\":\"" + tag + "\",\"limit\":" + limit + "}], \"id\":1}";
  }

  public static String discussionsByCreated(String tag) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\"," +
      " \"params\":[{\"tag\":\"" + tag + "\",\"limit\":8}], \"id\":1}";
  }

  public static String discussionsByCreated(String tag, String start_author, String start_permlink) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\"," +
      "\"params\":[{\"start_author\":\"" + start_author + "\",\"start_permlink\":\"" + start_permlink +
      "\",\"tag\":\"" + tag + "\",\"limit\":8}], \"id\":1}";
  }

  public static String contentReplies(String author, String permlink) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_content_replies\"," +
      " \"params\":[\"" + author + "\", \"" + permlink + "\"]," +
      " \"id\":1}";
  }

  public static String transactionState(String user) {
    return "{\"id\":5,\"jsonrpc\":\"2.0\",\"method\":\"call\",\"params\":[\"database_api\"," +
      "\"get_state\",[\"/@" + user + "/transfers\"]]}";
  }

  public static String lookupAccounts(String segment) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.lookup_accounts\"," +
      " \"params\":[\"" + segment + "\",10], \"id\":1}";
  }

  public static String globalProperties() {
    return "{\"id\":0,\"jsonrpc\":\"2.0\",\"method\":\"get_dynamic_global_properties\",\"params\":[]}";
  }

  public static String rewardFund() {
    return "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"get_reward_fund\",\"params\":[\"post\"]}";
  }

  public static String medianPriceHistory() {
    return "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"get_current_median_history_price\",\"params\":[]}";
  }

  public static String userAccounts(String[] users) {
    StringBuilder stringBuilder = new StringBuilder();
    int len = users.length;
    for (int i = 0; i < len; i++) {
      stringBuilder.append("\"").append(users[i]).append("\"");
      if (i != len - 1) {
        stringBuilder.append(",");
      }
    }
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_accounts\"," +
      " \"params\":[[" + stringBuilder.toString() + "]], \"id\":1}";
  }

  public static String followCountBody(String user) {
    return "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_follow_count\"," +
      " \"params\":[\"" + user + "\"]," +
      " \"id\":1}";
  }

  public static String followersListBody(String username, String startUser) {
    startUser = startUser == null ? null : "\"" + startUser + "\"";
    return "{\"jsonrpc\":\"2.0\"," +
      " \"method\":\"condenser_api.get_followers\"," +
      " \"params\":[\"" + username + "\"," + startUser + ",\"blog\",20], \"id\":1}";
  }

  public static String followingsListBody(String username, String startUser) {
    startUser = startUser == null ? null : "\"" + startUser + "\"";
    return "{\"jsonrpc\":\"2.0\"," +
      " \"method\":\"condenser_api.get_following\"," +
      " \"params\":[\"" + username + "\"," + startUser + ",\"blog\",20], \"id\":1}";
  }
}
