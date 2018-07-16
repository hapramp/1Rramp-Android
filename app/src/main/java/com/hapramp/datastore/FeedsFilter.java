package com.hapramp.datastore;

import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 5/2/2018.
 */

public class FeedsFilter {

  public static List<Feed> filter(List<Feed> feeds, String communityTag) {

    ArrayList<Feed> filteredFeedList = new ArrayList<>();
    List<String> tempTagList;
    String tag;

    // iterate through feeds
    for (int feedIndex = 0; feedIndex < feeds.size(); feedIndex++) {
      tempTagList = feeds.get(feedIndex).jsonMetadata.getTags();
      //iterate through tags
      for (int tagIndex = 0; tagIndex < tempTagList.size(); tagIndex++) {
        tag = tempTagList.get(tagIndex);
        if (tag.equals(communityTag)) {
          filteredFeedList.add(feeds.get(feedIndex));
        }
      }
    }

    return filteredFeedList;

  }

}
