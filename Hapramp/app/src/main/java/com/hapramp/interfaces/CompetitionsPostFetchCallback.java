package com.hapramp.interfaces;

import com.hapramp.CompetitionsPostReponse;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.models.response.CompetitionResponse;

/**
 * Created by Ankit on 11/13/2017.
 */

public interface CompetitionsPostFetchCallback {
    void onCompetitionsPostsFetched(CompetitionsPostReponse competitionResponse);
    void onCompetionsPostFetchError();
}
