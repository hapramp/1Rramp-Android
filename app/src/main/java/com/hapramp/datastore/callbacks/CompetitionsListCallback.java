package com.hapramp.datastore.callbacks;

import com.hapramp.models.CompetitionModel;

import java.util.List;

public interface CompetitionsListCallback {
  void onCompetitionsListAvailable(List<CompetitionModel> competitions);

  void onCompetitionsFetchError();
}
