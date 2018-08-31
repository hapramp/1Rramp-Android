package com.hapramp.steem;

import com.google.gson.Gson;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.List;

public class Communities {
  public static final String ALL = "all";
  public static final String TAG_HAPRAMP = "hapramp";
  public static final String IMAGE_URI_ALL = "https://user-images.githubusercontent.com/10809719/39564005-05b3a8cc-4ed0-11e8-8854-4714ef346798.png";

  public static boolean doesCommunityExists(String community_tag) {
    CommunityListWrapper communityListWrapper = new Gson().
      fromJson(HaprampPreferenceManager.getInstance().
        getAllCommunityAsJson(), CommunityListWrapper.class);
    if (communityListWrapper != null) {
      List<CommunityModel> communities = communityListWrapper.getCommunityModels();
      for (int i = 0; i < communities.size(); i++) {
        if (communities.get(i).getmTag().equals(community_tag)) {
          return true;
        }
      }
    }
    return false;
  }
}
