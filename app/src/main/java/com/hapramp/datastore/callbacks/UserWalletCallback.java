package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.User;

public interface UserWalletCallback {
  void whileWeAreFetchingWalletData();

  void onUser(User user);

  void onUserSteem(String steem);

  void onUserSteemDollar(String dollar);

  void onUserSteemPower(String steemPower);

  void onUserSavingSteem(String savingSteem);

  void onUserSavingSBD(String savingSBD);

  void onUserWalletDataError(String error);
}
