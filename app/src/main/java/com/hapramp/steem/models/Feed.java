package com.hapramp.steem.models;

/**
 * Created by Ankit on 3/8/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.models.data.ActiveVote;
import com.hapramp.steem.models.data.Beneficiary;
import com.hapramp.steem.models.data.JsonMetadata;

import java.util.ArrayList;
import java.util.List;


public class Feed implements Parcelable {

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
    @Override
    public Feed createFromParcel(Parcel in) {
      return new Feed(in);
    }

    @Override
    public Feed[] newArray(int size) {
      return new Feed[size];
    }
  };
  @SerializedName("id")
  @Expose
  public Long id;
  @SerializedName("author")
  @Expose
  public String author;
  @SerializedName("permlink")
  @Expose
  public String permlink;
  @SerializedName("category")
  @Expose
  public String category;
  @SerializedName("parent_author")
  @Expose
  public String parentAuthor;
  @SerializedName("parent_permlink")
  @Expose
  public String parentPermlink;
  @SerializedName("title")
  @Expose
  public String title;
  @SerializedName("body")
  @Expose
  public String body;
  @SerializedName("json_metadata")
  @Expose
  public JsonMetadata jsonMetadata;
  @SerializedName("last_update")
  @Expose
  public String lastUpdate;
  @SerializedName("created")
  @Expose
  public String created;
  @SerializedName("active")
  @Expose
  public String active;
  @SerializedName("last_payout")
  @Expose
  public String lastPayout;
  @SerializedName("depth")
  @Expose
  public Long depth;
  @SerializedName("children")
  @Expose
  public Long children;
  @SerializedName("net_rshares")
  @Expose
  public Long netRshares;
  @SerializedName("abs_rshares")
  @Expose
  public Long absRshares;
  @SerializedName("vote_rshares")
  @Expose
  public Long voteRshares;
  @SerializedName("children_abs_rshares")
  @Expose
  public Long childrenAbsRshares;
  @SerializedName("cashout_time")
  @Expose
  public String cashoutTime;
  @SerializedName("max_cashout_time")
  @Expose
  public String maxCashoutTime;
  @SerializedName("total_vote_weight")
  @Expose
  public Long totalVoteWeight;
  @SerializedName("reward_weight")
  @Expose
  public Long rewardWeight;
  @SerializedName("total_payout_value")
  @Expose
  public String totalPayoutValue;
  @SerializedName("curator_payout_value")
  @Expose
  public String curatorPayoutValue;
  @SerializedName("author_rewards")
  @Expose
  public Long authorRewards;
  @SerializedName("net_votes")
  @Expose
  public Long netVotes;
  @SerializedName("root_author")
  @Expose
  public String rootAuthor;
  @SerializedName("root_permlink")
  @Expose
  public String rootPermlink;
  @SerializedName("max_accepted_payout")
  @Expose
  public String maxAcceptedPayout;
  @SerializedName("percent_steem_dollars")
  @Expose
  public Long percentSteemDollars;
  @SerializedName("allow_replies")
  @Expose
  public Boolean allowReplies;
  @SerializedName("allow_votes")
  @Expose
  public Boolean allowVotes;
  @SerializedName("allow_curation_rewards")
  @Expose
  public Boolean allowCurationRewards;
  @SerializedName("beneficiaries")
  @Expose
  public List<Beneficiary> beneficiaries = new ArrayList<>();
  @SerializedName("url")
  @Expose
  public String url;
  @SerializedName("root_title")
  @Expose
  public String rootTitle;
  @SerializedName("pending_payout_value")
  @Expose
  public String pendingPayoutValue;
  @SerializedName("total_pending_payout_value")
  @Expose
  public String totalPendingPayoutValue;
  @SerializedName("active_votes")
  @Expose
  public List<ActiveVote> activeVotes = new ArrayList<>();
  @SerializedName("author_reputation")
  @Expose
  public Long authorReputation;
  @SerializedName("promoted")
  @Expose
  public String promoted;
  @SerializedName("body_length")
  @Expose
  public Long bodyLength;

  protected Feed(Parcel in) {
    id = in.readByte() == 0x00 ? null : in.readLong();
    author = in.readString();
    permlink = in.readString();
    category = in.readString();
    parentAuthor = in.readString();
    parentPermlink = in.readString();
    title = in.readString();
    body = in.readString();
    jsonMetadata = (JsonMetadata) in.readValue(JsonMetadata.class.getClassLoader());
    lastUpdate = in.readString();
    created = in.readString();
    active = in.readString();
    lastPayout = in.readString();
    depth = in.readByte() == 0x00 ? null : in.readLong();
    children = in.readByte() == 0x00 ? null : in.readLong();
    netRshares = in.readByte() == 0x00 ? null : in.readLong();
    absRshares = in.readByte() == 0x00 ? null : in.readLong();
    voteRshares = in.readByte() == 0x00 ? null : in.readLong();
    childrenAbsRshares = in.readByte() == 0x00 ? null : in.readLong();
    cashoutTime = in.readString();
    maxCashoutTime = in.readString();
    totalVoteWeight = in.readByte() == 0x00 ? null : in.readLong();
    rewardWeight = in.readByte() == 0x00 ? null : in.readLong();
    totalPayoutValue = in.readString();
    curatorPayoutValue = in.readString();
    authorRewards = in.readByte() == 0x00 ? null : in.readLong();
    netVotes = in.readByte() == 0x00 ? null : in.readLong();
    rootAuthor = in.readString();
    rootPermlink = in.readString();
    maxAcceptedPayout = in.readString();
    percentSteemDollars = in.readByte() == 0x00 ? null : in.readLong();
    byte allowRepliesVal = in.readByte();
    allowReplies = allowRepliesVal == 0x02 ? null : allowRepliesVal != 0x00;
    byte allowVotesVal = in.readByte();
    allowVotes = allowVotesVal == 0x02 ? null : allowVotesVal != 0x00;
    byte allowCurationRewardsVal = in.readByte();
    allowCurationRewards = allowCurationRewardsVal == 0x02 ? null : allowCurationRewardsVal != 0x00;
    if (in.readByte() == 0x01) {
      beneficiaries = new ArrayList<Beneficiary>();
      in.readList(beneficiaries, Beneficiary.class.getClassLoader());
    } else {
      beneficiaries = null;
    }
    url = in.readString();
    rootTitle = in.readString();
    pendingPayoutValue = in.readString();
    totalPendingPayoutValue = in.readString();
    if (in.readByte() == 0x01) {
      activeVotes = new ArrayList<ActiveVote>();
      in.readList(activeVotes, ActiveVote.class.getClassLoader());
    } else {
      activeVotes = null;
    }
    authorReputation = in.readByte() == 0x00 ? null : in.readLong();
    promoted = in.readString();
    bodyLength = in.readByte() == 0x00 ? null : in.readLong();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(id);
    }
    dest.writeString(author);
    dest.writeString(permlink);
    dest.writeString(category);
    dest.writeString(parentAuthor);
    dest.writeString(parentPermlink);
    dest.writeString(title);
    dest.writeString(body);
    dest.writeValue(jsonMetadata);
    dest.writeString(lastUpdate);
    dest.writeString(created);
    dest.writeString(active);
    dest.writeString(lastPayout);
    if (depth == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(depth);
    }
    if (children == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(children);
    }
    if (netRshares == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(netRshares);
    }
    if (absRshares == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(absRshares);
    }
    if (voteRshares == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(voteRshares);
    }
    if (childrenAbsRshares == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(childrenAbsRshares);
    }
    dest.writeString(cashoutTime);
    dest.writeString(maxCashoutTime);
    if (totalVoteWeight == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(totalVoteWeight);
    }
    if (rewardWeight == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(rewardWeight);
    }
    dest.writeString(totalPayoutValue);
    dest.writeString(curatorPayoutValue);
    if (authorRewards == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(authorRewards);
    }
    if (netVotes == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(netVotes);
    }
    dest.writeString(rootAuthor);
    dest.writeString(rootPermlink);
    dest.writeString(maxAcceptedPayout);
    if (percentSteemDollars == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(percentSteemDollars);
    }
    if (allowReplies == null) {
      dest.writeByte((byte) (0x02));
    } else {
      dest.writeByte((byte) (allowReplies ? 0x01 : 0x00));
    }
    if (allowVotes == null) {
      dest.writeByte((byte) (0x02));
    } else {
      dest.writeByte((byte) (allowVotes ? 0x01 : 0x00));
    }
    if (allowCurationRewards == null) {
      dest.writeByte((byte) (0x02));
    } else {
      dest.writeByte((byte) (allowCurationRewards ? 0x01 : 0x00));
    }
    if (beneficiaries == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(beneficiaries);
    }
    dest.writeString(url);
    dest.writeString(rootTitle);
    dest.writeString(pendingPayoutValue);
    dest.writeString(totalPendingPayoutValue);
    if (activeVotes == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(activeVotes);
    }
    if (authorReputation == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(authorReputation);
    }
    dest.writeString(promoted);
    if (bodyLength == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeLong(bodyLength);
    }
  }
}
