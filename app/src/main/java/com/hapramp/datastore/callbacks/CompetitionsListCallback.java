package com.hapramp.datastore.callbacks;

import com.hapramp.models.CompetitionListResponse;
import com.hapramp.models.CompetitionModel;

import java.util.List;

public interface CompetitionsListCallback {
  void onCompetitionsListAvailable(CompetitionListResponse competitions, boolean isAppendable);

  void onCompetitionsFetchError();
}
