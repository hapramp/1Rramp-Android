package com.hapramp.steem.models;

/**
 * Created by Ankit on 3/8/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.models.data.Beneficiaries;
import com.hapramp.steem.models.data.JsonMetadata;

import java.util.ArrayList;
import java.util.List;


public class Feed implements Parcelable {

    @Expose
    @SerializedName("hapramp_votes")
    public long haprampVotes;
    @Expose
    @SerializedName("hapramp_rating")
    public long haprampRating;
    @Expose
    @SerializedName("reblogged_by")
    public List<String> rebloggedBy;
    @Expose
    @SerializedName("body_length")
    public long bodyLength;
    @Expose
    @SerializedName("promoted")
    public String promoted;
    @Expose
    @SerializedName("author_reputation")
    public long authorReputation;
    @Expose
    @SerializedName("total_pending_payout_value")
    public String totalPendingPayoutValue;
    @Expose
    @SerializedName("pending_payout_value")
    public String pendingPayoutValue;
    @Expose
    @SerializedName("root_title")
    public String rootTitle;
    @Expose
    @SerializedName("url")
    public String url;
    @Expose
    @SerializedName("beneficiaries")
    public List<Beneficiaries> beneficiaries;
    @Expose
    @SerializedName("allow_curation_rewards")
    public boolean allowCurationRewards;
    @Expose
    @SerializedName("allow_votes")
    public boolean allowVotes;
    @Expose
    @SerializedName("allow_replies")
    public boolean allowReplies;
    @Expose
    @SerializedName("percent_steem_dollars")
    public long percentSteemDollars;
    @Expose
    @SerializedName("max_accepted_payout")
    public String maxAcceptedPayout;
    @Expose
    @SerializedName("root_comment")
    public long rootComment;
    @Expose
    @SerializedName("net_votes")
    public long netVotes;
    @Expose
    @SerializedName("author_rewards")
    public long authorRewards;
    @Expose
    @SerializedName("curator_payout_value")
    public String curatorPayoutValue;
    @Expose
    @SerializedName("total_payout_value")
    public String totalPayoutValue;
    @Expose
    @SerializedName("reward_weight")
    public long rewardWeight;
    @Expose
    @SerializedName("total_vote_weight")
    public long totalVoteWeight;
    @Expose
    @SerializedName("max_cashout_time")
    public String maxCashoutTime;
    @Expose
    @SerializedName("cashout_time")
    public String cashoutTime;
    @Expose
    @SerializedName("children_abs_rshares")
    public long childrenAbsRshares;
    @Expose
    @SerializedName("vote_rshares")
    public long voteRshares;
    @Expose
    @SerializedName("abs_rshares")
    public long absRshares;
    @Expose
    @SerializedName("net_rshares")
    public long netRshares;
    @Expose
    @SerializedName("children")
    public long children;
    @Expose
    @SerializedName("depth")
    public long depth;
    @Expose
    @SerializedName("last_payout")
    public String lastPayout;
    @Expose
    @SerializedName("active")
    public String active;
    @Expose
    @SerializedName("created")
    public String created;
    @Expose
    @SerializedName("last_update")
    public String lastUpdate;
    @Expose
    @SerializedName("json_metadata")
    public JsonMetadata jsonMetadata;
    @Expose
    @SerializedName("body")
    public String body;
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("parent_permlink")
    public String parentPermlink;
    @Expose
    @SerializedName("parent_author")
    public String parentAuthor;
    @Expose
    @SerializedName("category")
    public String category;
    @Expose
    @SerializedName("permlink")
    public String permlink;
    @Expose
    @SerializedName("author")
    public String author;
    @Expose
    @SerializedName("id")
    public long id;

    protected Feed(Parcel in) {
        haprampVotes = in.readLong();
        haprampRating = in.readLong();
        if (in.readByte() == 0x01) {
            rebloggedBy = new ArrayList<String>();
            in.readList(rebloggedBy, String.class.getClassLoader());
        } else {
            rebloggedBy = null;
        }
        bodyLength = in.readLong();
        promoted = in.readString();
        authorReputation = in.readLong();
        totalPendingPayoutValue = in.readString();
        pendingPayoutValue = in.readString();
        rootTitle = in.readString();
        url = in.readString();
        if (in.readByte() == 0x01) {
            beneficiaries = new ArrayList<Beneficiaries>();
            in.readList(beneficiaries, Beneficiaries.class.getClassLoader());
        } else {
            beneficiaries = null;
        }
        allowCurationRewards = in.readByte() != 0x00;
        allowVotes = in.readByte() != 0x00;
        allowReplies = in.readByte() != 0x00;
        percentSteemDollars = in.readLong();
        maxAcceptedPayout = in.readString();
        rootComment = in.readLong();
        netVotes = in.readLong();
        authorRewards = in.readLong();
        curatorPayoutValue = in.readString();
        totalPayoutValue = in.readString();
        rewardWeight = in.readLong();
        totalVoteWeight = in.readLong();
        maxCashoutTime = in.readString();
        cashoutTime = in.readString();
        childrenAbsRshares = in.readLong();
        voteRshares = in.readLong();
        absRshares = in.readLong();
        netRshares = in.readLong();
        children = in.readLong();
        depth = in.readLong();
        lastPayout = in.readString();
        active = in.readString();
        created = in.readString();
        lastUpdate = in.readString();
        jsonMetadata = (JsonMetadata) in.readValue(JsonMetadata.class.getClassLoader());
        body = in.readString();
        title = in.readString();
        parentPermlink = in.readString();
        parentAuthor = in.readString();
        category = in.readString();
        permlink = in.readString();
        author = in.readString();
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(haprampVotes);
        dest.writeLong(haprampRating);
        if (rebloggedBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rebloggedBy);
        }
        dest.writeLong(bodyLength);
        dest.writeString(promoted);
        dest.writeLong(authorReputation);
        dest.writeString(totalPendingPayoutValue);
        dest.writeString(pendingPayoutValue);
        dest.writeString(rootTitle);
        dest.writeString(url);
        if (beneficiaries == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(beneficiaries);
        }
        dest.writeByte((byte) (allowCurationRewards ? 0x01 : 0x00));
        dest.writeByte((byte) (allowVotes ? 0x01 : 0x00));
        dest.writeByte((byte) (allowReplies ? 0x01 : 0x00));
        dest.writeLong(percentSteemDollars);
        dest.writeString(maxAcceptedPayout);
        dest.writeLong(rootComment);
        dest.writeLong(netVotes);
        dest.writeLong(authorRewards);
        dest.writeString(curatorPayoutValue);
        dest.writeString(totalPayoutValue);
        dest.writeLong(rewardWeight);
        dest.writeLong(totalVoteWeight);
        dest.writeString(maxCashoutTime);
        dest.writeString(cashoutTime);
        dest.writeLong(childrenAbsRshares);
        dest.writeLong(voteRshares);
        dest.writeLong(absRshares);
        dest.writeLong(netRshares);
        dest.writeLong(children);
        dest.writeLong(depth);
        dest.writeString(lastPayout);
        dest.writeString(active);
        dest.writeString(created);
        dest.writeString(lastUpdate);
        dest.writeValue(jsonMetadata);
        dest.writeString(body);
        dest.writeString(title);
        dest.writeString(parentPermlink);
        dest.writeString(parentAuthor);
        dest.writeString(category);
        dest.writeString(permlink);
        dest.writeString(author);
        dest.writeLong(id);
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public JsonMetadata getJsonMetadata() {
        return jsonMetadata;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermlink() {
        return permlink;
    }

    public String getAuthor() {
        return author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}