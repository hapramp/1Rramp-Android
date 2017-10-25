package com.hapramp.interfaces;

import com.hapramp.models.response.SkillsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 10/22/2017.
 */

public interface FetchSkillsResponse {
    void onSkillsFetched(List<SkillsModel> skillsModels);
    void onSkillFetchError();
}
