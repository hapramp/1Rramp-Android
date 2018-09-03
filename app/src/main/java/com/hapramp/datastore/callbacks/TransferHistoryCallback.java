package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.TransferHistoryModel;

import java.util.ArrayList;

public interface TransferHistoryCallback {
  void onAccountTransferHistoryAvailable(ArrayList<TransferHistoryModel> historyModels);

  void onAccountTransferHistoryError(String error);
}
