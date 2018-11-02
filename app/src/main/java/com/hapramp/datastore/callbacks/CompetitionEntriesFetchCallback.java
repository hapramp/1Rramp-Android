package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.Feed;

import java.util.List;

public interface CompetitionEntriesFetchCallback {
  void onCompetitionsEntriesAvailable(List<Feed> entries);

  void onCompetitionsEntriesFetchError();
}
