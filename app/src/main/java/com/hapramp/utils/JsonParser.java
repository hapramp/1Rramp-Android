package com.hapramp.utils;

import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.Voter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
  MarkdownPreProcessor markdownPreProcessor;

  public JsonParser() {
    markdownPreProcessor = new MarkdownPreProcessor();
  }

  public ArrayList<Feed> parseFeed(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONObject ro = new JSONObject(response);
      JSONArray results = ro.getJSONArray("result");
      for (int i = 0; i < results.length(); i++) {
        feeds.add(parseCoreData((JSONObject) results.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return feeds;
  }

  private Feed parseCoreData(JSONObject rootObject) {
    Feed feed = new Feed();
    try {
      JSONObject jsonMetaDataObj = getJsonMetaDataObject(rootObject);
      //author
      String author = rootObject.getString("author");
      //permlink
      String permlink = rootObject.getString("permlink");
      //category
      String category = rootObject.getString("category");
      //parentAuthor
      String parentAuthor = rootObject.getString("parent_author");
      //parent permlink
      String parentPermlink = rootObject.getString("parent_permlink");
      //title
      String title = rootObject.getString("title");
      //body
      String body = rootObject.getString("body");
      //featured image
      String featureImageUrl = extractFeatureImageUrl(jsonMetaDataObj, body);
      //format
      String format = extractFormatFromJsonMetadata(jsonMetaDataObj);
      //tags
      ArrayList<String> tags = extractTags(jsonMetaDataObj);
      //createdAt
      String createdAt = rootObject.getString("created");
      //depth
      int depth = rootObject.getInt("depth");
      //children
      int children = rootObject.getInt("children");
      //totalPayoutValue
      String totalPayoutValue = rootObject.getString("total_payout_value");
      //curator payout value
      String curatorPayoutValue = rootObject.getString("curator_payout_value");
      //root author
      String rootAuthor = rootObject.getString("root_author");
      //root permlink
      String rootPermlink = rootObject.getString("root_permlink");
      //url
      String url = rootObject.getString("url");
      //pending payout value
      String pendingPayoutValue = rootObject.getString("pending_payout_value");
      //total pending payout value
      String totalPendingPayoutValue = rootObject.getString("total_pending_payout_value");
      //voters
      ArrayList<Voter> voters = readVoters(rootObject);
      //author reputation
      String autorReputation = rootObject.getString("author_reputation");
      feed.setBody(body);
      feed.setCleanedBody(getCleanedBody(body));
      feed.setAuthor(author);
      feed.setPermlink(permlink);
      feed.setCategory(category);
      feed.setParentAuthor(parentAuthor);
      feed.setParentPermlink(parentPermlink);
      feed.setTitle(title);
      feed.setFeaturedImageUrl(featureImageUrl);
      feed.setFormat(format);
      feed.setTags(tags);
      feed.setCreatedAt(createdAt);
      feed.setDepth(depth);
      feed.setChildren(children);
      feed.setTotalPayoutValue(totalPayoutValue);
      feed.setCuratorPayoutValue(curatorPayoutValue);
      feed.setRootAuthor(rootAuthor);
      feed.setRootPermlink(rootPermlink);
      feed.setUrl(url);
      feed.setPendingPayoutValue(pendingPayoutValue);
      feed.setTotalPendingPayoutValue(totalPendingPayoutValue);
      feed.setVoters(voters);
      feed.setAuthorReputation(autorReputation);
    }
    catch (JSONException e) {
    }
    return feed;
  }

  private JSONObject getJsonMetaDataObject(JSONObject jsonObject) throws JSONException {
    if (jsonObject.get("json_metadata") instanceof JSONObject) {
      return jsonObject.getJSONObject("json_metadata");
    } else {
      String js = jsonObject.getString("json_metadata");
      if (js.length() > 0) {
        return new JSONObject((String) jsonObject.get("json_metadata"));
      } else {
        return new JSONObject();
      }
    }
  }

  private String extractFeatureImageUrl(JSONObject json_metadata, String body) throws JSONException {
    String imageUrl;
    //try finding image from json metadata
    if (json_metadata.has("image")) {
      JSONArray images = json_metadata.getJSONArray("image");
      if (images.length() > 0) {
        return (String) images.get(0);
      }
    }
    //if not found, try searching inside body
    imageUrl = markdownPreProcessor.getFirstImageUrl(body);
    return imageUrl;
  }

  private String extractFormatFromJsonMetadata(JSONObject json_metadata) throws JSONException {
    if (json_metadata.has("format")) {
      return json_metadata.getString("format");
    }
    return "markdown";
  }

  private ArrayList<String> extractTags(JSONObject json_metadata) throws JSONException {
    ArrayList<String> tags = new ArrayList<>();
    if (json_metadata.has("tags")) {
      JSONArray jsonArray = json_metadata.getJSONArray("tags");
      for (int i = 0; i < jsonArray.length(); i++) {
        tags.add((String) jsonArray.get(i));
      }
    }
    return tags;
  }

  private ArrayList<Voter> readVoters(JSONObject rootObject) throws JSONException {
    ArrayList<Voter> voters = new ArrayList<>();
    JSONArray __voters = rootObject.getJSONArray("active_votes");
    for (int i = 0; i < __voters.length(); i++) {
      JSONObject __voter = __voters.getJSONObject(i);
      voters.add(new Voter(
        __voter.getString("voter"),
        __voter.getInt("percent"),
        __voter.getString("reputation"),
        __voter.getString("time")
      ));
    }
    return voters;
  }

  private String getCleanedBody(String dirtyBody) {
    return markdownPreProcessor.getCleanContent(dirtyBody);
  }
}
