package com.hapramp.utils;

import com.google.gson.Gson;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;

import java.util.ArrayList;

/**
 * Created by Ankit on 3/16/2018.
 */

public class HaprampTags {

  public static ArrayList<String> getAllTags() {
    ArrayList<String> tags = new ArrayList<>();
    String json = HaprampPreferenceManager.getInstance().getAllCommunityAsJson();
    CommunityListWrapper wr = new Gson().fromJson(json, CommunityListWrapper.class);
    for (CommunityModel com : wr.getCommunityModels()) {
      tags.add(com.getmTag());
    }
    return tags;
  }

  public static ArrayList<String> getUserSelectedTags() {

    ArrayList<String> tags = new ArrayList<>();
    String json = HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson();
    CommunityListWrapper wr = new Gson().fromJson(json, CommunityListWrapper.class);

    for (CommunityModel com : wr.getCommunityModels()) {
      tags.add(com.getmTag());
    }
    return tags;

  }


}
