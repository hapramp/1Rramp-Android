package com.hapramp.utils;

import com.hapramp.models.CommunityModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommunitySortUtils {
  public static void sortCommunity(List<CommunityModel> communityModels) {
    Collections.sort(communityModels, new Comparator<CommunityModel>() {
      @Override
      public int compare(CommunityModel communityModel, CommunityModel t1) {
        return communityModel.getmName().compareTo(t1.getmName());
      }
    });
  }
}
