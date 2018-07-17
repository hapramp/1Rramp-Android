package com.hapramp.utils;

public class SteemPowerCalc {
  public static double calculateSteemPower(String user_vesting_share, String total_vesting_fund_steem, String total_vesting_shares) {
    double totalVestingShares = Double.valueOf(total_vesting_shares.split(" ")[0]);
    double totalVestingFundSteem = Double.valueOf(total_vesting_fund_steem.split(" ")[0]);
    double userVestingShare = Double.valueOf(user_vesting_share.split(" ")[0]);
    return ((totalVestingFundSteem * userVestingShare) / totalVestingShares);
  }
}
