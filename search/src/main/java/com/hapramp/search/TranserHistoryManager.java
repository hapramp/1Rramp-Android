package com.hapramp.search;

public class TranserHistoryManager implements NetworkUtils.NetworkResponseCallback {
		private NetworkUtils networkUtils;

		public TranserHistoryManager() {
				networkUtils = new NetworkUtils();
				networkUtils.setNetworkResponseCallback(this);
		}

		public void requestRawTransferHistory(String user) {
				String url = "https://api.steemit.com";
				String method = "POST";
				String body = "{\"id\":5,\"jsonrpc\":\"2.0\",\"method\":\"call\",\"params\":[\"database_api\",\"get_state\",[\"/@" + user + "/transfers\"]]}";
				networkUtils.request(url, method, body);
		}

		@Override
		public void onResponse(String response) {
				if (transferHistoryCallback != null) {
						transferHistoryCallback.onRawTransferResponse(response);
				}
		}

		@Override
		public void onError(String e) {
				if (transferHistoryCallback != null) {
						transferHistoryCallback.onRawTransferResponse(e);
				}
		}

		private TransferHistoryCallback transferHistoryCallback;

		public void setTransferHistoryCallback(TransferHistoryCallback transferHistoryCallback) {
				this.transferHistoryCallback = transferHistoryCallback;
		}

		public interface TransferHistoryCallback {
				void onRawTransferResponse(String response);

				void onRawTransferResponseError(String e);
		}
		
		public static class KEYS {
				public static final String KEY_RESULT = "result";
				public static final String KEY_ACCOUNTS = "accounts";
				public static final String KEY_TRANSFER_HISTORY = "transfer_history";
				public static final String KEY_OPERATION = "op";
				public static final String KEY_TIMESTAMP = "transfer_history";

				//operation: Transfer
				public static final String OPERATION_TRANSFER = "transfer";
				public static final String KEY_FROM = "from";
				public static final String KEY_TO = "to";
				public static final String KEY_AMOUNT = "amount";
				public static final String KEY_MEMO = "memo";

				//operation: Author Reward
				public static final String OPERATION_AUTHOR_REWARD = "author_reward";
				public static final String KEY_AUTHOR = "author";
				public static final String KEY_PERMLINK = "permlink";
				public static final String KEY_SBD_PAYOUT = "sbd_payout";
				public static final String KEY_STEEM_PAYOUT = "steem_payout";
				public static final String KEY_VESTING_PAYOUT = "vesting_payout";

				//operation: Comment Benefactor Reward
				public static final String OPERATION_COMMENT_BENEFACTOR_REWARD = "comment_benefactor_reward";
				public static final String KEY_BENEFACTOR = "benefactor";
				public static final String KEY_REWARD = "reward";

				//operation: claim reward balance
				public static final String OPERATION_CLAIM_REWARD_BALANCE = "claim_reward_balance";
				public static final String KEY_ACCOUNT = "account";
				public static final String KEY_REWARD_STEEM = "reward_steem";
				public static final String KEY_REWARD_SBD = "reward_sbd";
				public static final String KEY_REWARD_VESTS = "reward_vests";

				//operation : curation reward
				public static final String OPERATION_CURATION_REWARD = "curation_reward";
				public static final String KEY_CURATOR = "curator";
				public static final String KEY_COMMENT_AUTHOR = "comment_author";
				public static final String KEY_COMMENT_PERMLINK = "comment_permlink";

		}
}

