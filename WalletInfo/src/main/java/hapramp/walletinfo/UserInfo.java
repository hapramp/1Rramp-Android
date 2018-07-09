package hapramp.walletinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

		@Expose
		@SerializedName("user")
		private User user;

		public static class User {
				@Expose
				@SerializedName("posting_rewards")
				private long posting_rewards;
				@Expose
				@SerializedName("curation_rewards")
				private long curation_rewards;
				@Expose
				@SerializedName("withdraw_routes")
				private long withdraw_routes;
				@Expose
				@SerializedName("to_withdraw")
				private long to_withdraw;
				@Expose
				@SerializedName("withdrawn")
				private long withdrawn;
				@Expose
				@SerializedName("reward_steem_balance")
				private String reward_steem_balance;
				@Expose
				@SerializedName("reward_sbd_balance")
				private String reward_sbd_balance;
				@Expose
				@SerializedName("savings_withdraw_requests")
				private long savings_withdraw_requests;
				@Expose
				@SerializedName("savings_sbd_seconds")
				private String savings_sbd_seconds;
				@Expose
				@SerializedName("savings_sbd_balance")
				private String savings_sbd_balance;
				@Expose
				@SerializedName("sbd_seconds")
				private String sbd_seconds;
				@Expose
				@SerializedName("sbd_balance")
				private String sbd_balance;
				@Expose
				@SerializedName("savings_balance")
				private String savings_balance;
				@Expose
				@SerializedName("balance")
				private String balance;
				@Expose
				@SerializedName("vesting_shares")
				private String vesting_share;
				@Expose
				@SerializedName("voting_power")
				private long voting_power;
				@Expose
				@SerializedName("post_count")
				private long post_count;
				@Expose
				@SerializedName("json_metadata")
				private Json_metadata json_metadata;

				public long getPosting_rewards() {
						return posting_rewards;
				}

				public long getCuration_rewards() {
						return curation_rewards;
				}

				public long getWithdraw_routes() {
						return withdraw_routes;
				}

				public long getTo_withdraw() {
						return to_withdraw;
				}

				public long getWithdrawn() {
						return withdrawn;
				}

				public String getReward_steem_balance() {
						return reward_steem_balance;
				}

				public String getVesting_share() {
						return vesting_share;
				}

				public String getReward_sbd_balance() {
						return reward_sbd_balance;
				}

				public long getSavings_withdraw_requests() {
						return savings_withdraw_requests;
				}

				public String getSavings_sbd_seconds() {
						return savings_sbd_seconds;
				}

				public String getSavings_sbd_balance() {
						return savings_sbd_balance;
				}

				public String getSbd_seconds() {
						return sbd_seconds;
				}

				public String getSbd_balance() {
						return sbd_balance;
				}

				public String getSavings_balance() {
						return savings_balance;
				}

				public String getBalance() {
						return balance;
				}

				public long getVoting_power() {
						return voting_power;
				}

				public long getPost_count() {
						return post_count;
				}

				public Json_metadata getJson_metadata() {
						return json_metadata;
				}
		}

		public static class Json_metadata {
				@Expose
				@SerializedName("profile")
				private Profile profile;

				public Profile getProfile() {
						return profile;
				}
		}

		public static class Profile {
				@Expose
				@SerializedName("location")
				private String location;
				@Expose
				@SerializedName("about")
				private String about;
				@Expose
				@SerializedName("name")
				private String name;
				@Expose
				@SerializedName("cover_image")
				private String cover_image;
				@Expose
				@SerializedName("profile_image")
				private String profile_image;

				public String getLocation() {
						return location;
				}

				public String getAbout() {
						return about;
				}

				public String getName() {
						return name;
				}

				public String getCover_image() {
						return cover_image;
				}

				public String getProfile_image() {
						return profile_image;
				}
		}

		public User getUser() {
				return user;
		}
}
