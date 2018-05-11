package com.hapramp.interfaces;

import com.hapramp.datamodels.response.CompetitionResponse;

import java.util.List;

/**
 * Created by Ankit on 10/28/2017.
 */

public interface CompetitionFetchCallback {

    void onCompetitionsFetched(List<CompetitionResponse> competitionResponses);
    void onCompetitionsFetchError();

}
