package com.hapramp.datastore.callbacks;

import com.hapramp.models.JudgeModel;

import java.util.ArrayList;
import java.util.List;

public interface JudgesListFetchFromServerCallback {
  void onJudgesListAvailable(ArrayList<JudgeModel> judgeModelList);

  void onJudgesFetchError(String msg);

}
