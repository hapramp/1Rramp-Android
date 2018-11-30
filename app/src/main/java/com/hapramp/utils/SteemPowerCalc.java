package com.hapramp.utils;

import com.google.gson.Gson;
import com.hapramp.models.GlobalProperties;
import com.hapramp.preferences.HaprampPreferenceManager;

public class SteemPowerCalc {
  public static double calculateSteemPower(double user_vesting_share,
                                           String total_vesting_fund_steem,
                                           String total_vesting_shares) {
    double totalVestingShares = Double.valueOf(total_vesting_shares.split(" ")[0]);
    double totalVestingFundSteem = Double.valueOf(total_vesting_fund_steem.split(" ")[0]);
    double vestsPerSteem = totalVestingShares / totalVestingFundSteem;
    HaprampPreferenceManager.getInstance().saveVestsPerSteem(vestsPerSteem);
    return user_vesting_share / vestsPerSteem;
  }

  public static double calculateSteemPower(double user_vesting_share) {
    String globalPropsJson = HaprampPreferenceManager.getInstance().getGlobalProps();
    if (globalPropsJson != null) {
      GlobalProperties globalProperties = new Gson().fromJson(globalPropsJson, GlobalProperties.class);
      String total_vesting_fund_steem = globalProperties.getResult().getTotal_vesting_fund_steem();
      String total_vesting_shares = globalProperties.getResult().getTotal_vesting_shares();
      double totalVestingShares = Double.valueOf(total_vesting_shares.split(" ")[0]);
      double totalVestingFundSteem = Double.valueOf(total_vesting_fund_steem.split(" ")[0]);
      double vestsPerSteem = totalVestingShares / totalVestingFundSteem;
      HaprampPreferenceManager.getInstance().saveVestsPerSteem(vestsPerSteem);
      return user_vesting_share / vestsPerSteem;
    }
    return 0;
  }

}
