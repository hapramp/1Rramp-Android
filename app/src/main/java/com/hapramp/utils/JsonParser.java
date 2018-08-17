package com.hapramp.utils;

import android.util.Log;

import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.Voter;
import com.hapramp.steem.models.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
  MarkdownPreProcessor markdownPreProcessor;

  public JsonParser() {
    markdownPreProcessor = new MarkdownPreProcessor();
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
      String url = rootObject.optString("url", "");
      //pending payout value
      String pendingPayoutValue = rootObject.optString("pending_payout_value", "");
      //total pending payout value
      String totalPendingPayoutValue = rootObject.optString("total_pending_payout_value", "");
      //voters
      ArrayList<Voter> voters = readVoters(rootObject);
      //author reputation
      String autorReputation = rootObject.optString("author_reputation", "");
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
      Log.e("JsonParserException", e.toString());
    }
    return feed;
  }

  private ArrayList<Voter> readVoters(JSONObject rootObject) throws JSONException {
    ArrayList<Voter> voters = new ArrayList<>();
    if (rootObject.has("active_votes")) {
      JSONArray __voters = rootObject.optJSONArray("active_votes");
      for (int i = 0; i < __voters.length(); i++) {
        JSONObject __voter = __voters.getJSONObject(i);
        voters.add(new Voter(
          __voter.getString("voter"),
          __voter.getInt("percent"),
          __voter.getString("reputation"),
          __voter.getString("time")
        ));
      }
    }
    return voters;
  }

  //{"jsonrpc":"2.0","result":[{"id
  public ArrayList<Feed> parseFeedStructure3(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONObject ro = new JSONObject(response);
      JSONArray feedsArray = ro.getJSONArray("result");
      for (int i = 0; i < feedsArray.length(); i++) {
        feeds.add(parseCoreData((JSONObject) feedsArray.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return feeds;
  }

  // {"posts": [{...
  public ArrayList<Feed> parseFeedStructure2(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONObject root = new JSONObject(response);
      JSONArray jsonArray = root.getJSONArray("posts");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject co = jsonArray.getJSONObject(i);
        feeds.add(parseCoreData(co));
      }
    }
    catch (JSONException e) {

    }
    return feeds;
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
        Object im = images.get(0);
        if (im instanceof String) {
          return (String) im;
        }
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
        try {
          if (jsonArray.get(i) instanceof String) {
            tags.add((String) jsonArray.get(i));
          } else {
            continue;
          }
        }catch (Exception e) {
          continue;
        }
      }
    }
    return tags;
  }

  public ArrayList<Feed> parseCuratedFeed(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONArray rootArray = new JSONArray(response);
      for (int i = 0; i < rootArray.length(); i++) {
        feeds.add(parseCoreData((JSONObject) rootArray.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return feeds;
  }

  private String getCleanedBody(String dirtyBody) {
    return markdownPreProcessor.getCleanContent(dirtyBody);
  }

  public User parseUser(String userJson) {
    User user = new User();
    try {
      JSONObject root = new JSONObject(userJson);
      JSONObject userObj = root.getJSONObject("user");
      JSONObject jmd = userObj.getJSONObject("json_metadata");
      if (jmd.has("profile")) {
        JSONObject po = jmd.getJSONObject("profile");
        if (po.has("profile_image")) {
          user.setProfile_image(po.getString("profile_image"));
        }
        if (po.has("cover_image")) {
          user.setCover_image("cover_image");
        }
        if (po.has("name")) {
          user.setFullname(po.getString("name"));
        }
        if (po.has("location")) {
          user.setLocation("location");
        }
        if (po.has("about")) {
          user.setAbout(po.getString("about"));
        }
        if (po.has("website")) {
          user.setWebsite(po.getString("website"));
        }
      }
      user.setCreated(userObj.getString("created"));
      user.setCommentCount(userObj.getInt("comment_count"));
      user.setPostCount(userObj.optInt("post_count"));
      user.setCanVote(userObj.getBoolean("can_vote"));
      user.setVotingPower(userObj.getInt("voting_power"));
      user.setReputation(userObj.getLong("reputation"));
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }
}
