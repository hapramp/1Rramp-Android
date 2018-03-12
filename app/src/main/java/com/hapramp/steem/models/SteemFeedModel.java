package com.hapramp.steem.models;

/**
 * Created by Ankit on 3/8/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SteemFeedModel {

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

    public static class Beneficiaries {
        @Expose
        @SerializedName("weight")
        public int weight;
        @Expose
        @SerializedName("account")
        public String account;

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

    public static class JsonMetadata {
        @Expose
        @SerializedName("tags")
        public List<String> tags;
        @Expose
        @SerializedName("content")
        public Content content;
        @Expose
        @SerializedName("app")
        public String app;

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

    public static class Content {
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("data")
        public List<Data> data;

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

    public static class Data {
        @Expose
        @SerializedName("type")
        public String type;
        @Expose
        @SerializedName("content")
        public String content;

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