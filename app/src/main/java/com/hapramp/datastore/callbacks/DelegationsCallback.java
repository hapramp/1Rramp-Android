package com.hapramp.datastore.callbacks;

import com.hapramp.models.DelegationModel;

import java.util.ArrayList;

public interface DelegationsCallback {
  void onDelegationsFetched(ArrayList<DelegationModel> delegationModels);

  void onDelegationsFetchFailed();
}
