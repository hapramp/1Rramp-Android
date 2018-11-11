package com.hapramp.datastore;

import android.util.Log;

import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CompetitionAdmin;
import com.hapramp.models.CompetitionModel;
import com.hapramp.models.DelegationModel;
import com.hapramp.models.JudgeModel;
import com.hapramp.models.ResourceCreditModel;
import com.hapramp.models.VestedShareModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.User;
import com.hapramp.steem.models.Voter;
import com.hapramp.utils.Constants;
import com.hapramp.utils.MarkdownPreProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {
  private MarkdownPreProcessor markdownPreProcessor;

  public JSONParser() {
    markdownPreProcessor = new MarkdownPreProcessor();
  }

  public List<CompetitionModel> parseCompetitionList(String response) {
    List<CompetitionModel> cps = new ArrayList<>();
    CompetitionModel competitionModel = null;
    try {
      JSONArray carray = new JSONArray(response);
      for (int i = 0; i < carray.length(); i++) {
        competitionModel = new CompetitionModel();
        JSONObject comp_item = carray.getJSONObject(i);
        CompetitionAdmin competitionAdmin = new CompetitionAdmin();
        //parse creater
        competitionAdmin.setmId(comp_item.getJSONObject("user").optInt("id"));
        competitionAdmin.setmUsername(comp_item.getJSONObject("user").optString("username"));
        //parse other info
        competitionModel.setmId(comp_item.optString("id"));
        //participating_tag
        competitionModel.setmParticipationHashtag(comp_item.optString("participating_tag"));
        competitionModel.setmParticipantCount(comp_item.optInt("participant_count"));
        competitionModel.setmPostCount(comp_item.optInt("post_count"));
        competitionModel.setmAdmin(competitionAdmin);
        competitionModel.setmCreatedAt(comp_item.optString("created_at"));
        competitionModel.setmImage(comp_item.optString("image"));
        competitionModel.setmTitle(comp_item.optString("title"));
        competitionModel.setmDescription(comp_item.optString("description"));
        competitionModel.setmStartsAt(comp_item.optString("starts_at"));
        competitionModel.setmEndsAt(comp_item.optString("ends_at"));
        competitionModel.setmRules(comp_item.optString("rules"));
        competitionModel.setmJudges(parseJudgesJsonArray(comp_item.getJSONArray("judges")));
        competitionModel.setCommunities(parseAllCommunity(comp_item.getJSONArray("communities").toString()));
        competitionModel.setPrizes(parsePrizes(comp_item.getJSONArray("prizes")));
        competitionModel.setWinners_announced(comp_item.getBoolean("winners_announced"));
        cps.add(competitionModel);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return cps;
  }

  private ArrayList<JudgeModel> parseJudgesJsonArray(JSONArray array) {
    ArrayList<JudgeModel> judgeModels = new ArrayList<>();
    try {
      JudgeModel jm;
      for (int i = 0; i < array.length(); i++) {
        JSONObject j = array.getJSONObject(i);
        jm = new JudgeModel();
        jm.setmBio(j.optString("bio"));
        jm.setmFullName(j.optString("full_name"));
        jm.setmId(j.optInt("id"));
        jm.setmUsername(j.optString("username"));
        judgeModels.add(jm);
      }
    }
    catch (JSONException e) {
      Log.d("JSONException", e.toString());
    }
    return judgeModels;
  }

  public ArrayList<CommunityModel> parseAllCommunity(String response) {
    ArrayList<CommunityModel> communityModels = new ArrayList<>();
    try {
      JSONObject jsonObject;
      JSONArray jsonArray = new JSONArray(response);
      for (int i = 0; i < jsonArray.length(); i++) {
        jsonObject = jsonArray.getJSONObject(i);
        communityModels.add(new CommunityModel(
          jsonObject.getString("description"),
          jsonObject.getString("image_uri"),
          jsonObject.getString("tag"),
          jsonObject.getString("color"),
          jsonObject.getString("name"),
          jsonObject.getInt("id")));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return communityModels;
  }

  private List<String> parsePrizes(JSONArray prizesJsonArray) throws JSONException {
    List<String> prizes = new ArrayList<>();
    for (int i = 0; i < prizesJsonArray.length(); i++) {
      prizes.add(prizesJsonArray.getString(i));
    }
    return prizes;
  }

  public ArrayList<JudgeModel> parseJudges(String response) {
    try {
      JSONArray jsonArray = new JSONArray(response);
      return parseJudgesJsonArray(jsonArray);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  public void parseCompetitionEligibilityResponse(String response) {
    try {
      JSONObject ro = new JSONObject(response);
      parseAndStoreEligibility(ro);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void parseAndStoreEligibility(JSONObject jsonObject) throws JSONException {
    if (jsonObject.has("is_competition_user")) {
      boolean eligible = jsonObject.getBoolean("is_competition_user");
      Log.d("ParsingCompetition", "eligible " + eligible);
      HaprampPreferenceManager.getInstance().setCompetitionCreateEligibility(eligible);
    }
  }

  public List<CommunityModel> parseUserCommunity(String response) {
    List<CommunityModel> communityModels = new ArrayList<>();
    try {
      JSONObject ro = new JSONObject(response);
      // TODO: 23/10/18 remove comment when migrating to production api
      //parseAndStoreEligibility(ro);
      JSONArray jsonArray = ro.getJSONArray("communities");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        communityModels.add(new CommunityModel(
          jsonObject.getString("description"),
          jsonObject.getString("image_uri"),
          jsonObject.getString("tag"),
          jsonObject.getString("color"),
          jsonObject.getString("name"),
          jsonObject.getInt("id")));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return communityModels;
  }

  public ArrayList<Feed> parseUserFeed(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONObject root = new JSONObject(response);
      JSONArray jsonArray = root.getJSONArray("posts");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject co = jsonArray.getJSONObject(i);
        feeds.add(parseCoreFeedData(co));
      }
    }
    catch (JSONException e) {

    }
    return feeds;
  }

  private Feed parseCoreFeedData(JSONObject rootObject) {
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
      body = body.split(Constants.FOOTER_START_MARK)[0];
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
      String cashoutTime = rootObject.optString("cashout_time");
      //voters
      ArrayList<Voter> voters = readVoters(rootObject);
      ArrayList<Voter> activeVoters = extractActiveVoters(voters);
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
      feed.setCashOutTime(cashoutTime);
      feed.setVoters(voters);
      feed.setActiveVoters(activeVoters);
      feed.setAuthorReputation(autorReputation);
      //optional
      feed.setRank(rootObject.optInt("rank", 0));
      //optional
      feed.setPrize(rootObject.optString("prize", "Prize"));
    }
    catch (JSONException e) {
      Log.e("JsonParserException", e.toString());
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
        }
        catch (Exception e) {
          continue;
        }
      }
    }
    return tags;
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

  private ArrayList<Voter> extractActiveVoters(ArrayList<Voter> voters) {
    ArrayList<Voter> av = new ArrayList<>();
    for (int i = 0; i < voters.size(); i++) {
      if (voters.get(i).getPercent() > 0) {
        av.add(voters.get(i));
      }
    }
    return av;
  }

  private String getCleanedBody(String dirtyBody) {
    return markdownPreProcessor.getCleanContent(dirtyBody);
  }

  public ArrayList<Feed> parseExplorePosts(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONArray jsonArray = new JSONArray(response);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject co = jsonArray.getJSONObject(i);
        feeds.add(parseCoreFeedData(co));
      }
    }
    catch (JSONException e) {

    }
    return feeds;
  }

  public ArrayList<Feed> parseSteemFeed(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONObject ro = new JSONObject(response);
      JSONArray feedsArray = ro.getJSONArray("result");
      for (int i = 0; i < feedsArray.length(); i++) {
        feeds.add(parseCoreFeedData((JSONObject) feedsArray.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return feeds;
  }

  public ArrayList<Feed> parseCuratedFeed(String response) {
    ArrayList<Feed> feeds = new ArrayList<>();
    try {
      JSONArray rootArray = new JSONArray(response);
      for (int i = 0; i < rootArray.length(); i++) {
        feeds.add(parseCoreFeedData((JSONObject) rootArray.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return feeds;
  }

  public User parseRawUserJson(String userJson) {
    User user = new User();
    try {
      JSONObject root = new JSONObject(userJson);
      JSONObject userObj = root.getJSONObject("user");
      user = parseCoreUserJson(userObj);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }

  public User parseCoreUserJson(JSONObject userObj) {
    User user = new User();
    try {
      JSONObject jmd = getJsonMetaDataObject(userObj);
      if (jmd.has("profile")) {
        JSONObject po = jmd.getJSONObject("profile");
        user.setProfile_image(po.optString("profile_image", ""));
        user.setCover_image(po.optString("cover_image", ""));
        user.setFullname(po.optString("name", ""));
        user.setLocation(po.optString("location", ""));
        user.setAbout(po.optString("about", ""));
        user.setWebsite(po.optString("website", ""));
      } else {
        user.setProfile_image("");
        user.setCover_image("");
        user.setFullname("");
        user.setLocation("");
        user.setAbout("");
        user.setWebsite("");
      }
      user.setUsername(userObj.getString("name"));
      user.setCreated(userObj.getString("created"));
      user.setCommentCount(userObj.getInt("comment_count"));
      user.setPostCount(userObj.optInt("post_count"));
      user.setCanVote(userObj.getBoolean("can_vote"));
      user.setVotingPower(userObj.getInt("voting_power"));
      user.setReputation(userObj.getLong("reputation"));
      user.setSavings_sbd_balance(userObj.getString("savings_sbd_balance"));
      user.setSbd_balance(userObj.getString("sbd_balance"));
      user.setSavings_balance(userObj.getString("savings_balance"));
      user.setSbdRewardBalance(userObj.getString("reward_sbd_balance"));
      user.setSteemRewardBalance(userObj.getString("reward_steem_balance"));
      user.setVestsRewardBalance(userObj.getString("reward_vesting_balance"));
      user.setReceived_vesting_shares(userObj.getString("received_vesting_shares"));
      user.setDelegated_vesting_shares(userObj.getString("delegated_vesting_shares"));
      user.setBalance(userObj.getString("balance"));
      user.setVesting_share(userObj.getString("vesting_shares"));
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }

  public User parseSC2UserJson(String response) {
    User user = new User();
    try {
      JSONObject root = new JSONObject(response);
      JSONObject userObj = root.getJSONObject("account");
      user = parseCoreUserJson(userObj);
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
  }

  public ArrayList<CommentModel> parseComments(String response) {
    ArrayList<CommentModel> comments = new ArrayList<>();
    try {
      JSONObject ro = new JSONObject(response);
      JSONArray commentsArray = ro.getJSONArray("result");
      for (int i = 0; i < commentsArray.length(); i++) {
        comments.add(parseCoreComment((JSONObject) commentsArray.get(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return comments;
  }

  private CommentModel parseCoreComment(JSONObject rootObject) {
    CommentModel comment = new CommentModel();
    try {
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
      //body
      String body = rootObject.getString("body");
      //createdAt
      String createdAt = rootObject.getString("created");
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
      //pending payout value
      String pendingPayoutValue = rootObject.optString("pending_payout_value", "");
      //total pending payout value
      String totalPendingPayoutValue = rootObject.optString("total_pending_payout_value", "");
      String cashoutTime = rootObject.optString("cashout_time");
      //author reputation
      String autorReputation = rootObject.optString("author_reputation", "");
      comment.setBody(body);
      comment.setBody(body);
      comment.setAuthor(author);
      comment.setPermlink(permlink);
      comment.setCategory(category);
      comment.setParentAuthor(parentAuthor);
      comment.setParentPermlink(parentPermlink);
      comment.setCreatedAt(createdAt);
      comment.setChildren(children);
      comment.setTotalPayoutValue(totalPayoutValue);
      comment.setCuratorPayoutValue(curatorPayoutValue);
      comment.setPendingPayoutValue(pendingPayoutValue);
      comment.setTotalPendingPayoutValue(totalPendingPayoutValue);
      comment.setCashoutTime(cashoutTime);
    }
    catch (JSONException e) {
      Log.e("JsonParserException", e.toString());
    }
    return comment;
  }

  public ArrayList<VestedShareModel> parseAllUsersVestedShare(String response) {
    ArrayList<VestedShareModel> vestedShareModels = new ArrayList<>();
    try {
      JSONArray results = new JSONObject(response).getJSONArray("result");
      for (int i = 0; i < results.length(); i++) {
        JSONObject uo = results.getJSONObject(i);
        String username = uo.getString("name");
        String receivedVests = uo.getString("received_vesting_shares");
        String delegatedVests = uo.getString("delegated_vesting_shares");
        String userVests = uo.getString("vesting_shares");
        int vp = uo.getInt("voting_power");
        vestedShareModels.add(new VestedShareModel(userVests,
          username,
          vp,
          receivedVests,
          delegatedVests));
      }
    }
    catch (JSONException e) {

    }
    return vestedShareModels;
  }

  public Feed parseSingleFeed(String singlePostResponse) {
    Feed feed = null;
    try {
      JSONObject jsonObject = new JSONObject(singlePostResponse);
      feed = parseCoreFeedData(jsonObject.getJSONObject("result"));
    }
    catch (JSONException e) {
      Log.d("JSONException", e.toString());
    }
    return feed;
  }

  public List<Feed> parseCompetitionEntries(String response) {
    List<Feed> entries = new ArrayList<>();
    try {
      JSONArray jsonArray = new JSONArray(response);
      for (int i = 0; i < jsonArray.length(); i++) {
        entries.add(parseCoreFeedData(jsonArray.getJSONObject(i)));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return entries;
  }

  public ArrayList<DelegationModel> parseDelegations(String response) {
    ArrayList<DelegationModel> delegationModels = new ArrayList<>();
    try {
      JSONObject jsonObject = new JSONObject(response);
      JSONArray delegationsArray = jsonObject.getJSONArray("result");
      for (int i = 0; i < delegationsArray.length(); i++) {
        JSONObject item = delegationsArray.getJSONObject(i);
        delegationModels.add(new DelegationModel(
          item.optString("delegator", ""),
          item.optString("delegatee", ""),
          item.optString("vesting_shares", "0 VESTS"),
          item.optString("min_delegation_time", "")
        ));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return delegationModels;
  }

  public ResourceCreditModel parseRc(String response) {
    ResourceCreditModel creditModel = null;
    try {
      JSONObject jsonObject = new JSONObject(response);
      creditModel = new ResourceCreditModel();
      creditModel.setCommentAllowed(jsonObject.getInt("comment_count"));
      creditModel.setTransferAllowed(jsonObject.getInt("transfer_count"));
      creditModel.setVoteALlowed(jsonObject.getInt("vote_count"));
      creditModel.setVotingPercentage(jsonObject.getInt("voting_power_percentage"));
      creditModel.setResourceCreditPercentage(jsonObject.getInt("rc_mana_percentage"));
      creditModel.setVoteValue(jsonObject.getDouble("vote_value"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return creditModel;
  }
}
