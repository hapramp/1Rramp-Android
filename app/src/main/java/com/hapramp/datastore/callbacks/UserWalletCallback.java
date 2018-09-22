package com.hapramp.datastore.callbacks;

import com.hapramp.steem.models.User;

public interface UserWalletCallback {
  void onFetchingWalletInfo();

  void onUser(User user);

  void onUserSteem(String steem);

  void onUserSteemDollar(String dollar);

  void onUserSteemPower(String steemPowerOwned, String steemPowerDelegated, String steemPowerReceived);

  void onUserSavingSteem(String savingSteem);

  void onUserSavingSBD(String savingSBD);

  void onUserRewards(String sbdReward, String steemReward, String rewardVests);

  void onUserWalletDataError(String error);
}
