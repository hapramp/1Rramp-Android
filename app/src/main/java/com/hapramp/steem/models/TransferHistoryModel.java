package com.hapramp.steem.models;

public class TransferHistoryModel {
		private String userAccount;
		private String operation;
		private Transfer transfer;
  private AuthorReward authorReward;
  private CommentBenefactor commentBenefactor;
  private ClaimRewardBalance claimRewardBalance;
  private CurationReward curationReward;
		private String timeStamp;

		public TransferHistoryModel() { }

		public String getUserAccount() {
				return userAccount;
		}

		public void setUserAccount(String userAccount) {
				this.userAccount = userAccount;
		}

		public void setOperation(String operation) {
				this.operation = operation;
		}

		public void setTransfer(Transfer transfer) {
				this.transfer = transfer;
		}

		public void setAuthorReward(AuthorReward authorReward) {
				this.authorReward = authorReward;
		}

		public void setCommentBenefactor(CommentBenefactor commentBenefactor) {
				this.commentBenefactor = commentBenefactor;
		}

		public void setClaimRewardBalance(ClaimRewardBalance claimRewardBalance) {
				this.claimRewardBalance = claimRewardBalance;
		}

		public void setCurationReward(CurationReward curationReward) {
				this.curationReward = curationReward;
		}

		public String getOperation() {
				return operation;
		}

		public String getTimeStamp() {
				return timeStamp;
		}

		public Transfer getTransfer() {
				return transfer;
		}

		public AuthorReward getAuthorReward() {
				return authorReward;
		}

		public CommentBenefactor getCommentBenefactor() {
				return commentBenefactor;
		}

		public ClaimRewardBalance getClaimRewardBalance() {
				return claimRewardBalance;
		}

		public CurationReward getCurationReward() {
				return curationReward;
		}

		public void setTimeStamp(String timeStamp) {
				this.timeStamp = timeStamp;
		}

		public static class Transfer{
				public String from;
				public String to;
				public String amount;
				public String memo;

				public Transfer(String from, String to, String amount, String memo) {
						this.from = from;
						this.to = to;
						this.amount = amount;
						this.memo = memo;
				}

				public String getFrom() {
						return from;
				}

				public String getTo() {
						return to;
				}

				public String getAmount() {
						return amount;
				}

				public String getMemo() {
						return memo;
				}

				@Override
				public String toString() {
						return "Transfer{" +
								"from='" + from + '\'' +
								", to='" + to + '\'' +
								", amount='" + amount + '\'' +
								", memo='" + memo + '\'' +
								'}';
				}
		}

		public static class AuthorReward{
				public String author;
				public String permlink;
				public String sbd_payout;
				public String steem_payout;
				public String vesting_payout;

				public AuthorReward(String author, String permlink, String sbd_payout, String steem_payout, String vesting_payout) {
						this.author = author;
						this.permlink = permlink;
						this.sbd_payout = sbd_payout;
						this.steem_payout = steem_payout;
						this.vesting_payout = vesting_payout;
				}

				public String getAuthor() {
						return author;
				}

				public String getPermlink() {
						return permlink;
				}

				public String getSbd_payout() {
						return sbd_payout;
				}

				public String getSteem_payout() {
						return steem_payout;
				}

				public String getVesting_payout() {
						return vesting_payout;
				}

				@Override
				public String toString() {
						return "AuthorReward{" +
								"author='" + author + '\'' +
								", permlink='" + permlink + '\'' +
								", sbd_payout='" + sbd_payout + '\'' +
								", steem_payout='" + steem_payout + '\'' +
								", vesting_payout='" + vesting_payout + '\'' +
								'}';
				}
		}

		public static class CommentBenefactor{
				private String benefactor;
				private String author;
				private String permlink;
				private String reward;

				public CommentBenefactor(String benefactor, String author, String permlink, String reward) {
						this.benefactor = benefactor;
						this.author = author;
						this.permlink = permlink;
						this.reward = reward;
				}

				public String getBenefactor() {
						return benefactor;
				}

				public String getAuthor() {
						return author;
				}

				public String getPermlink() {
						return permlink;
				}

				public String getReward() {
						return reward;
				}

				@Override
				public String toString() {
						return "CommentBenefactor{" +
								"benefactor='" + benefactor + '\'' +
								", author='" + author + '\'' +
								", permlink='" + permlink + '\'' +
								", reward='" + reward + '\'' +
								'}';
				}
		}

		public static class ClaimRewardBalance {
				private String account;
				private String reward_steem;
				private String reward_sbd;
				private String reward_vests;

				public ClaimRewardBalance(String account, String reward_steem, String reward_sbd, String reward_vests) {
						this.account = account;
						this.reward_steem = reward_steem;
						this.reward_sbd = reward_sbd;
						this.reward_vests = reward_vests;
				}

				public String getAccount() {
						return account;
				}

				public String getReward_steem() {
						return reward_steem;
				}

				public String getReward_sbd() {
						return reward_sbd;
				}

				public String getReward_vests() {
						return reward_vests;
				}

				@Override
				public String toString() {
						return "ClaimRewardBalance{" +
								"account='" + account + '\'' +
								", reward_steem='" + reward_steem + '\'' +
								", reward_sbd='" + reward_sbd + '\'' +
								", reward_vests='" + reward_vests + '\'' +
								'}';
				}
		}

		public static class CurationReward {
				private String curator;
				private String reward;
				private String comment_author;
				private String comment_permlink;

				public CurationReward(String curator, String reward, String comment_author, String comment_permlink) {
						this.curator = curator;
						this.reward = reward;
						this.comment_author = comment_author;
						this.comment_permlink = comment_permlink;
				}

				public String getCurator() {
						return curator;
				}

				public String getReward() {
						return reward;
				}

				public String getComment_author() {
						return comment_author;
				}

				public String getComment_permlink() {
						return comment_permlink;
				}

				@Override
				public String toString() {
						return "CurationReward{" +
								"curator='" + curator + '\'' +
								", reward='" + reward + '\'' +
								", comment_author='" + comment_author + '\'' +
								", comment_permlink='" + comment_permlink + '\'' +
								'}';
				}
		}

		@Override
		public String toString() {
				return "TransferHistoryModel{" +
						"operation='" + operation + '\'' +
						", transfer=" + transfer +
						", authorReward=" + authorReward +
						", commentBenefactor=" + commentBenefactor +
						", claimRewardBalance=" + claimRewardBalance +
						", curationReward=" + curationReward +
						'}';
		}
}
