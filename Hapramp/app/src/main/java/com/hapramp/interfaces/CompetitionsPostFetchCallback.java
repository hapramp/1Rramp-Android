package com.hapramp.interfaces;

import com.hapramp.models.response.CompetitionsPostReponse;

/**
 * Created by Ankit on 11/13/2017.
 */

public interface CompetitionsPostFetchCallback {
    void onCompetitionsPostsFetched(CompetitionsPostReponse competitionResponse);
    void onCompetionsPostFetchError();
}
