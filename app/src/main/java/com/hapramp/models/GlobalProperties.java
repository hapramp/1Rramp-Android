package com.hapramp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalProperties {

  @Expose
  @SerializedName("result")
  private Result result;

  public Result getResult() {
    return result;
  }

  public static class Result {
    @Expose
    @SerializedName("total_reward_fund_steem")
    private String total_reward_fund_steem;
    @Expose
    @SerializedName("total_vesting_shares")
    private String total_vesting_shares;
    @Expose
    @SerializedName("total_vesting_fund_steem")
    private String total_vesting_fund_steem;

    public String getTotal_reward_fund_steem() {
      return total_reward_fund_steem;
    }

    public String getTotal_vesting_shares() {
      return total_vesting_shares;
    }

    public String getTotal_vesting_fund_steem() {
      return total_vesting_fund_steem;
    }
  }
}
