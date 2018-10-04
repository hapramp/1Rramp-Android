package com.hapramp.utils;

import java.util.ArrayList;
import java.util.List;

public class PostHashTagPreprocessor {
  public static ArrayList<String> processHashtags(List<String> hashtags) {
    ArrayList<String> newTags = new ArrayList<>();
    //rule: Include hapramp as first tag
    //rule: Add equivalent tags for each community e.g. hapramp-art -> art + hapramp-art
    newTags.add("hapramp");
    for (String ht : hashtags) {
      newTags.add(ht.replace("hapramp-",""));
    }
    //add older tags
    newTags.addAll(hashtags);
    return newTags;
  }
}
