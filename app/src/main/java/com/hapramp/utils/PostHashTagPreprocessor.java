package com.hapramp.utils;

import java.util.ArrayList;
import java.util.List;

public class PostHashTagPreprocessor {
  public static ArrayList<String> processHashtags(List<String> hashtags) {
    ArrayList<String> newTags = new ArrayList<>();
    //rule: Include hapramp as first tag
    //rule: Add equivalent tags for each community e.g. hapramp-art -> art + hapramp-art
    String temp = "";
    newTags.add("hapramp");
    for (String ht : hashtags) {
      if (ht.contains("hapramp-")) {
        temp = ht.replace("hapramp-", "");
        if (!newTags.contains(temp)) {
          newTags.add(temp);
          //also add origin hapramp tag
          newTags.add(ht);
        }
      } else {
        if (!newTags.contains(ht)) {
          newTags.add(ht);
        }
      }
    }
    return newTags;
  }
}
