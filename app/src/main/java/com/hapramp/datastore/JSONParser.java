package com.hapramp.datastore;

import com.hapramp.models.CommunityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {
  public List<CommunityModel> parseUserCommunity(String response) {
    List<CommunityModel> communityModels = new ArrayList<>();
    try {
      JSONObject jsonObject = new JSONObject(response);
      JSONArray jsonArray = jsonObject.getJSONArray("communities");
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
}
