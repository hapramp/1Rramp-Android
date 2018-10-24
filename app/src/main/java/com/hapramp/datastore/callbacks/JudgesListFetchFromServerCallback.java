package com.hapramp.datastore.callbacks;

import com.hapramp.models.JudgeModel;

import java.util.List;

public interface JudgesListFetchFromServerCallback {
  void onJudgesListAvailable(List<JudgeModel> judgeModelList);

  void onJudgesFetchError(String msg);

}
