package com.hapramp.utils;

import com.hapramp.preferences.HaprampPreferenceManager;

public class SteemPowerCalc {
  public static double calculateSteemPower(double user_vesting_share, String total_vesting_fund_steem, String total_vesting_shares) {
    double totalVestingShares = Double.valueOf(total_vesting_shares.split(" ")[0]);
    double totalVestingFundSteem = Double.valueOf(total_vesting_fund_steem.split(" ")[0]);
    double vestsPerSteem = totalVestingShares / totalVestingFundSteem;
    HaprampPreferenceManager.getInstance().saveVestsPerSteem(vestsPerSteem);
    return user_vesting_share / vestsPerSteem;
  }
}
