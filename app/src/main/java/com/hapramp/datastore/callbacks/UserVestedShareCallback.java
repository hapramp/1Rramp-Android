package com.hapramp.datastore.callbacks;

import com.hapramp.models.VestedShareModel;

import java.util.ArrayList;

public interface UserVestedShareCallback {
  void onUserAccountsVestedShareDataAvailable(ArrayList<VestedShareModel> vestedShareModels);

  void onVestedShareDataError(String err);
}
