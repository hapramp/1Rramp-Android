package com.hapramp.datastore.callbacks;

public interface RewardFundMedianPriceCallback {
  void onError(String err);

  void onFeedHistoryDataAvailable(String reward_balance, String reward_claim,
                                  String base, String quote);
}
