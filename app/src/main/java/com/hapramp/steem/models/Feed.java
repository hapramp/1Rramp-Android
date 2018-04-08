package com.hapramp.steem.models;

/**
 * Created by Ankit on 3/8/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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


    public int getHaprampVotes() {
        return haprampVotes;
    }

    public void setHaprampVotes(int haprampVotes) {
        this.haprampVotes = haprampVotes;
    }

    public int getHaprampRating() {
        return haprampRating;
    }

    public void setHaprampRating(int haprampRating) {
        this.haprampRating = haprampRating;
    }

    public List<String> getRebloggedBy() {
        return rebloggedBy;
    }

    public void setRebloggedBy(List<String> rebloggedBy) {
        this.rebloggedBy = rebloggedBy;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public String getPromoted() {
        return promoted;
    }

    public void setPromoted(String promoted) {
        this.promoted = promoted;
    }

    public int getAuthorReputation() {
        return authorReputation;
    }

    public void setAuthorReputation(int authorReputation) {
        this.authorReputation = authorReputation;
    }

    public String getTotalPendingPayoutValue() {
        return totalPendingPayoutValue;
    }

    public void setTotalPendingPayoutValue(String totalPendingPayoutValue) {
        this.totalPendingPayoutValue = totalPendingPayoutValue;
    }

    public String getPendingPayoutValue() {
        return pendingPayoutValue;
    }

    public void setPendingPayoutValue(String pendingPayoutValue) {
        this.pendingPayoutValue = pendingPayoutValue;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Beneficiaries> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiaries> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public boolean isAllowCurationRewards() {
        return allowCurationRewards;
    }

    public void setAllowCurationRewards(boolean allowCurationRewards) {
        this.allowCurationRewards = allowCurationRewards;
    }

    public boolean isAllowVotes() {
        return allowVotes;
    }

    public void setAllowVotes(boolean allowVotes) {
        this.allowVotes = allowVotes;
    }

    public boolean isAllowReplies() {
        return allowReplies;
    }

    public void setAllowReplies(boolean allowReplies) {
        this.allowReplies = allowReplies;
    }

    public int getPercentSteemDollars() {
        return percentSteemDollars;
    }

    public void setPercentSteemDollars(int percentSteemDollars) {
        this.percentSteemDollars = percentSteemDollars;
    }

    public String getMaxAcceptedPayout() {
        return maxAcceptedPayout;
    }

    public void setMaxAcceptedPayout(String maxAcceptedPayout) {
        this.maxAcceptedPayout = maxAcceptedPayout;
    }

    public int getRootComment() {
        return rootComment;
    }

    public void setRootComment(int rootComment) {
        this.rootComment = rootComment;
    }

    public int getNetVotes() {
        return netVotes;
    }

    public void setNetVotes(int netVotes) {
        this.netVotes = netVotes;
    }

    public int getAuthorRewards() {
        return authorRewards;
    }

    public void setAuthorRewards(int authorRewards) {
        this.authorRewards = authorRewards;
    }

    public String getCuratorPayoutValue() {
        return curatorPayoutValue;
    }

    public void setCuratorPayoutValue(String curatorPayoutValue) {
        this.curatorPayoutValue = curatorPayoutValue;
    }

    public String getTotalPayoutValue() {
        return totalPayoutValue;
    }

    public void setTotalPayoutValue(String totalPayoutValue) {
        this.totalPayoutValue = totalPayoutValue;
    }

    public int getRewardWeight() {
        return rewardWeight;
    }

    public void setRewardWeight(int rewardWeight) {
        this.rewardWeight = rewardWeight;
    }

    public int getTotalVoteWeight() {
        return totalVoteWeight;
    }

    public void setTotalVoteWeight(int totalVoteWeight) {
        this.totalVoteWeight = totalVoteWeight;
    }

    public String getMaxCashoutTime() {
        return maxCashoutTime;
    }

    public void setMaxCashoutTime(String maxCashoutTime) {
        this.maxCashoutTime = maxCashoutTime;
    }

    public String getCashoutTime() {
        return cashoutTime;
    }

    public void setCashoutTime(String cashoutTime) {
        this.cashoutTime = cashoutTime;
    }

    public int getChildrenAbsRshares() {
        return childrenAbsRshares;
    }

    public void setChildrenAbsRshares(int childrenAbsRshares) {
        this.childrenAbsRshares = childrenAbsRshares;
    }

    public int getVoteRshares() {
        return voteRshares;
    }

    public void setVoteRshares(int voteRshares) {
        this.voteRshares = voteRshares;
    }

    public int getAbsRshares() {
        return absRshares;
    }

    public void setAbsRshares(int absRshares) {
        this.absRshares = absRshares;
    }

    public int getNetRshares() {
        return netRshares;
    }

    public void setNetRshares(int netRshares) {
        this.netRshares = netRshares;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getLastPayout() {
        return lastPayout;
    }

    public void setLastPayout(String lastPayout) {
        this.lastPayout = lastPayout;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public JsonMetadata getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(JsonMetadata jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
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

    public String getParentPermlink() {
        return parentPermlink;
    }

    public void setParentPermlink(String parentPermlink) {
        this.parentPermlink = parentPermlink;
    }

    public String getParentAuthor() {
        return parentAuthor;
    }

    public void setParentAuthor(String parentAuthor) {
        this.parentAuthor = parentAuthor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPermlink() {
        return permlink;
    }

    public void setPermlink(String permlink) {
        this.permlink = permlink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class Beneficiaries implements Parcelable {
        @Expose
        @SerializedName("weight")
        public int weight;
        @Expose
        @SerializedName("account")
        public String account;

        protected Beneficiaries(Parcel in) {
            weight = in.readInt();
            account = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(weight);
            dest.writeString(account);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Beneficiaries> CREATOR = new Parcelable.Creator<Beneficiaries>() {
            @Override
            public Beneficiaries createFromParcel(Parcel in) {
                return new Beneficiaries(in);
            }

            @Override
            public Beneficiaries[] newArray(int size) {
                return new Beneficiaries[size];
            }
        };

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }

    public static class JsonMetadata implements Parcelable {
        @Expose
        @SerializedName("tags")
        public List<String> tags;
        @Expose
        @SerializedName("content")
        public Content content;
        @Expose
        @SerializedName("app")
        public String app;

        protected JsonMetadata(Parcel in) {
            if (in.readByte() == 0x01) {
                tags = new ArrayList<String>();
                in.readList(tags, String.class.getClassLoader());
            } else {
                tags = null;
            }
            content = (Content) in.readValue(Content.class.getClassLoader());
            app = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (tags == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeList(tags);
            }
            dest.writeValue(content);
            dest.writeString(app);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<JsonMetadata> CREATOR = new Parcelable.Creator<JsonMetadata>() {
            @Override
            public JsonMetadata createFromParcel(Parcel in) {
                return new JsonMetadata(in);
            }

            @Override
            public JsonMetadata[] newArray(int size) {
                return new JsonMetadata[size];
            }
        };

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }
    }

    public static class Content implements Parcelable {
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("data")
        public List<Data> data;

        protected Content(Parcel in) {
            type = in.readString();
            if (in.readByte() == 0x01) {
                data = new ArrayList<Data>();
                in.readList(data, Data.class.getClassLoader());
            } else {
                data = null;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            if (data == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeList(data);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
            @Override
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            @Override
            public Content[] newArray(int size) {
                return new Content[size];
            }
        };

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
    }

    public static class Data implements Parcelable {
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("content")
        public String content;

        protected Data(Parcel in) {
            type = in.readString();
            content = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type);
            dest.writeString(content);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}