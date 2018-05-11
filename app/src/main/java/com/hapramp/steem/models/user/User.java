package com.hapramp.steem.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 3/27/2018.
 */

public class User {

    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("memo_key")
    @Expose
    public String memoKey;
    @SerializedName("json_metadata")
    @Expose
    public UserProfileJsonMetadata jsonMetadata;
    @SerializedName("last_owner_update")
    @Expose
    public String lastOwnerUpdate;
    @SerializedName("last_account_update")
    @Expose
    public String lastAccountUpdate;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("mined")
    @Expose
    public Boolean mined;
    @SerializedName("recovery_account")
    @Expose
    public String recoveryAccount;
    @SerializedName("last_account_recovery")
    @Expose
    public String lastAccountRecovery;
    @SerializedName("reset_account")
    @Expose
    public String resetAccount;
    @SerializedName("comment_count")
    @Expose
    public Long commentCount;
    @SerializedName("lifetime_vote_count")
    @Expose
    public Long lifetimeVoteCount;
    @SerializedName("post_count")
    @Expose
    public Long postCount;
    @SerializedName("can_vote")
    @Expose
    public Boolean canVote;
    @SerializedName("voting_power")
    @Expose
    public Long votingPower;
    @SerializedName("last_vote_time")
    @Expose
    public String lastVoteTime;
    @SerializedName("balance")
    @Expose
    public String balance;
    @SerializedName("savings_balance")
    @Expose
    public String savingsBalance;
    @SerializedName("sbd_balance")
    @Expose
    public String sbdBalance;
    @SerializedName("sbd_seconds")
    @Expose
    public String sbdSeconds;
    @SerializedName("sbd_seconds_last_update")
    @Expose
    public String sbdSecondsLastUpdate;
    @SerializedName("sbd_last_interest_payment")
    @Expose
    public String sbdLastInterestPayment;
    @SerializedName("savings_sbd_balance")
    @Expose
    public String savingsSbdBalance;
    @SerializedName("savings_sbd_seconds")
    @Expose
    public String savingsSbdSeconds;
    @SerializedName("savings_sbd_seconds_last_update")
    @Expose
    public String savingsSbdSecondsLastUpdate;
    @SerializedName("savings_sbd_last_interest_payment")
    @Expose
    public String savingsSbdLastInterestPayment;
    @SerializedName("savings_withdraw_requests")
    @Expose
    public Long savingsWithdrawRequests;
    @SerializedName("reward_sbd_balance")
    @Expose
    public String rewardSbdBalance;
    @SerializedName("reward_steem_balance")
    @Expose
    public String rewardSteemBalance;
    @SerializedName("reward_vesting_balance")
    @Expose
    public String rewardVestingBalance;
    @SerializedName("reward_vesting_steem")
    @Expose
    public String rewardVestingSteem;
    @SerializedName("vesting_shares")
    @Expose
    public String vestingShares;
    @SerializedName("delegated_vesting_shares")
    @Expose
    public String delegatedVestingShares;
    @SerializedName("received_vesting_shares")
    @Expose
    public String receivedVestingShares;
    @SerializedName("vesting_withdraw_rate")
    @Expose
    public String vestingWithdrawRate;
    @SerializedName("next_vesting_withdrawal")
    @Expose
    public String nextVestingWithdrawal;
    @SerializedName("withdrawn")
    @Expose
    public Long withdrawn;
    @SerializedName("to_withdraw")
    @Expose
    public Long toWithdraw;
    @SerializedName("withdraw_routes")
    @Expose
    public Long withdrawRoutes;
    @SerializedName("curation_rewards")
    @Expose
    public Long curationRewards;
    @SerializedName("posting_rewards")
    @Expose
    public Long postingRewards;
    @SerializedName("witnesses_voted_for")
    @Expose
    public Long witnessesVotedFor;
    @SerializedName("last_post")
    @Expose
    public String lastPost;
    @SerializedName("last_root_post")
    @Expose
    public String lastRootPost;
    @SerializedName("average_bandwidth")
    @Expose
    public Long averageBandwidth;
    @SerializedName("lifetime_bandwidth")
    @Expose
    public String lifetimeBandwidth;
    @SerializedName("last_bandwidth_update")
    @Expose
    public String lastBandwidthUpdate;
    @SerializedName("average_market_bandwidth")
    @Expose
    public Long averageMarketBandwidth;
    @SerializedName("lifetime_market_bandwidth")
    @Expose
    public Long lifetimeMarketBandwidth;
    @SerializedName("last_market_bandwidth_update")
    @Expose
    public String lastMarketBandwidthUpdate;
    @SerializedName("vesting_balance")
    @Expose
    public String vestingBalance;
    @SerializedName("reputation")
    @Expose
    public Long reputation;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMemoKey() {
        return memoKey;
    }

    public UserProfileJsonMetadata getJsonMetadata() {
        return jsonMetadata;
    }

    public String getLastOwnerUpdate() {
        return lastOwnerUpdate;
    }

    public String getLastAccountUpdate() {
        return lastAccountUpdate;
    }

    public String getCreated() {
        return created;
    }

    public Boolean getMined() {
        return mined;
    }

    public String getRecoveryAccount() {
        return recoveryAccount;
    }

    public String getLastAccountRecovery() {
        return lastAccountRecovery;
    }

    public String getResetAccount() {
        return resetAccount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public Long getLifetimeVoteCount() {
        return lifetimeVoteCount;
    }

    public Long getPostCount() {
        return postCount;
    }

    public Boolean getCanVote() {
        return canVote;
    }

    public Long getVotingPower() {
        return votingPower;
    }

    public String getLastVoteTime() {
        return lastVoteTime;
    }

    public String getBalance() {
        return balance;
    }

    public String getSavingsBalance() {
        return savingsBalance;
    }

    public String getSbdBalance() {
        return sbdBalance;
    }

    public String getSbdSeconds() {
        return sbdSeconds;
    }

    public String getSbdSecondsLastUpdate() {
        return sbdSecondsLastUpdate;
    }

    public String getSbdLastInterestPayment() {
        return sbdLastInterestPayment;
    }

    public String getSavingsSbdBalance() {
        return savingsSbdBalance;
    }

    public String getSavingsSbdSeconds() {
        return savingsSbdSeconds;
    }

    public String getSavingsSbdSecondsLastUpdate() {
        return savingsSbdSecondsLastUpdate;
    }

    public String getSavingsSbdLastInterestPayment() {
        return savingsSbdLastInterestPayment;
    }

    public Long getSavingsWithdrawRequests() {
        return savingsWithdrawRequests;
    }

    public String getRewardSbdBalance() {
        return rewardSbdBalance;
    }

    public String getRewardSteemBalance() {
        return rewardSteemBalance;
    }

    public String getRewardVestingBalance() {
        return rewardVestingBalance;
    }

    public String getRewardVestingSteem() {
        return rewardVestingSteem;
    }

    public String getVestingShares() {
        return vestingShares;
    }

    public String getDelegatedVestingShares() {
        return delegatedVestingShares;
    }

    public String getReceivedVestingShares() {
        return receivedVestingShares;
    }

    public String getVestingWithdrawRate() {
        return vestingWithdrawRate;
    }

    public String getNextVestingWithdrawal() {
        return nextVestingWithdrawal;
    }

    public Long getWithdrawn() {
        return withdrawn;
    }

    public Long getToWithdraw() {
        return toWithdraw;
    }

    public Long getWithdrawRoutes() {
        return withdrawRoutes;
    }

    public Long getCurationRewards() {
        return curationRewards;
    }

    public Long getPostingRewards() {
        return postingRewards;
    }

    public Long getWitnessesVotedFor() {
        return witnessesVotedFor;
    }

    public String getLastPost() {
        return lastPost;
    }

    public String getLastRootPost() {
        return lastRootPost;
    }

    public Long getAverageBandwidth() {
        return averageBandwidth;
    }

    public String getLifetimeBandwidth() {
        return lifetimeBandwidth;
    }

    public String getLastBandwidthUpdate() {
        return lastBandwidthUpdate;
    }

    public Long getAverageMarketBandwidth() {
        return averageMarketBandwidth;
    }

    public Long getLifetimeMarketBandwidth() {
        return lifetimeMarketBandwidth;
    }

    public String getLastMarketBandwidthUpdate() {
        return lastMarketBandwidthUpdate;
    }

    public String getVestingBalance() {
        return vestingBalance;
    }

    public Long getReputation() {
        return reputation;
    }
}