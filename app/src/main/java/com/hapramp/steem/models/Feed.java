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
    public int haprampVotes;
    @Expose
    @SerializedName("hapramp_rating")
    public int haprampRating;
    @Expose
    @SerializedName("reblogged_by")
    public List<String> rebloggedBy;
    @Expose
    @SerializedName("body_length")
    public int bodyLength;
    @Expose
    @SerializedName("promoted")
    public String promoted;
    @Expose
    @SerializedName("author_reputation")
    public int authorReputation;
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
    public int percentSteemDollars;
    @Expose
    @SerializedName("max_accepted_payout")
    public String maxAcceptedPayout;
    @Expose
    @SerializedName("root_comment")
    public int rootComment;
    @Expose
    @SerializedName("net_votes")
    public int netVotes;
    @Expose
    @SerializedName("author_rewards")
    public int authorRewards;
    @Expose
    @SerializedName("curator_payout_value")
    public String curatorPayoutValue;
    @Expose
    @SerializedName("total_payout_value")
    public String totalPayoutValue;
    @Expose
    @SerializedName("reward_weight")
    public int rewardWeight;
    @Expose
    @SerializedName("total_vote_weight")
    public int totalVoteWeight;
    @Expose
    @SerializedName("max_cashout_time")
    public String maxCashoutTime;
    @Expose
    @SerializedName("cashout_time")
    public String cashoutTime;
    @Expose
    @SerializedName("children_abs_rshares")
    public int childrenAbsRshares;
    @Expose
    @SerializedName("vote_rshares")
    public int voteRshares;
    @Expose
    @SerializedName("abs_rshares")
    public int absRshares;
    @Expose
    @SerializedName("net_rshares")
    public int netRshares;
    @Expose
    @SerializedName("children")
    public int children;
    @Expose
    @SerializedName("depth")
    public int depth;
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
    public int id;

    protected Feed(Parcel in) {
        haprampVotes = in.readInt();
        haprampRating = in.readInt();
        if (in.readByte() == 0x01) {
            rebloggedBy = new ArrayList<String>();
            in.readList(rebloggedBy, String.class.getClassLoader());
        } else {
            rebloggedBy = null;
        }
        bodyLength = in.readInt();
        promoted = in.readString();
        authorReputation = in.readInt();
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
        percentSteemDollars = in.readInt();
        maxAcceptedPayout = in.readString();
        rootComment = in.readInt();
        netVotes = in.readInt();
        authorRewards = in.readInt();
        curatorPayoutValue = in.readString();
        totalPayoutValue = in.readString();
        rewardWeight = in.readInt();
        totalVoteWeight = in.readInt();
        maxCashoutTime = in.readString();
        cashoutTime = in.readString();
        childrenAbsRshares = in.readInt();
        voteRshares = in.readInt();
        absRshares = in.readInt();
        netRshares = in.readInt();
        children = in.readInt();
        depth = in.readInt();
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
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(haprampVotes);
        dest.writeInt(haprampRating);
        if (rebloggedBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rebloggedBy);
        }
        dest.writeInt(bodyLength);
        dest.writeString(promoted);
        dest.writeInt(authorReputation);
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
        dest.writeInt(percentSteemDollars);
        dest.writeString(maxAcceptedPayout);
        dest.writeInt(rootComment);
        dest.writeInt(netVotes);
        dest.writeInt(authorRewards);
        dest.writeString(curatorPayoutValue);
        dest.writeString(totalPayoutValue);
        dest.writeInt(rewardWeight);
        dest.writeInt(totalVoteWeight);
        dest.writeString(maxCashoutTime);
        dest.writeString(cashoutTime);
        dest.writeInt(childrenAbsRshares);
        dest.writeInt(voteRshares);
        dest.writeInt(absRshares);
        dest.writeInt(netRshares);
        dest.writeInt(children);
        dest.writeInt(depth);
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
        dest.writeInt(id);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}