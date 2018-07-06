package hapramp.walletinfo;

import com.google.gson.Gson;

import java.util.Locale;

public class Wallet {

		double sbd_usd = 0;
		double steem_usd = 0;
		private NetworkUtils mNetworkUtils;
		private UserInfo userInfo;

		public Wallet() {
				mNetworkUtils = new NetworkUtils();
		}

		public void requestUserAccount(final String userAccount) {
				String url = "https://steemit.com/@" + userAccount + ".json";
				mNetworkUtils.setNetworkResponseCallback(new NetworkUtils.NetworkResponseCallback() {

						@Override
						public void onResponse(String response) {
								userInfo = new Gson().fromJson(response, UserInfo.class);
								requestSBDRates();
						}

						@Override
						public void onError(String e) {
								if (userAccountFieldsCallback != null) {
										userAccountFieldsCallback.onError(e);
								}
						}
				});
				mNetworkUtils.request(url, "GET", "");
		}

		private void requestSBDRates() {
				String url = "https://min-api.cryptocompare.com/data/histoday?fsym=SBD*&tsym=USD&limit=6";
				mNetworkUtils.setNetworkResponseCallback(new NetworkUtils.NetworkResponseCallback() {
						@Override
						public void onResponse(String response) {
								ConversionModel conversionModel = new Gson().fromJson(response, ConversionModel.class);
								sbd_usd = conversionModel.getData().get(0).getClose();
								requestSteemRates();
						}

						@Override
						public void onError(String e) {
								if (userAccountFieldsCallback != null) {
										userAccountFieldsCallback.onError(e);
								}
						}
				});
				mNetworkUtils.request(url, "GET", "");
		}

		private void requestSteemRates() {
				String url = "https://min-api.cryptocompare.com/data/histoday?fsym=STEEM&tsym=USD&limit=6";
				mNetworkUtils.setNetworkResponseCallback(new NetworkUtils.NetworkResponseCallback() {
						@Override
						public void onResponse(String response) {
								ConversionModel conversionModel = new Gson().fromJson(response, ConversionModel.class);
								steem_usd = conversionModel.getData().get(0).getClose();
								requestGlobalProperties();
						}

						@Override
						public void onError(String e) {
								if (userAccountFieldsCallback != null) {
										userAccountFieldsCallback.onError(e);
								}
						}
				});
				mNetworkUtils.request(url, "GET", "");
		}

		private void requestGlobalProperties() {
				String url = "https://api.steemit.com";
				String body = "{\"id\":0,\"jsonrpc\":\"2.0\",\"method\":\"get_dynamic_global_properties\",\"params\":[]}";
				mNetworkUtils.setNetworkResponseCallback(new NetworkUtils.NetworkResponseCallback() {
						@Override
						public void onResponse(String response) {
								GlobalProperties globalProperties = new Gson().fromJson(response, GlobalProperties.class);
								sendBackResults(globalProperties);
						}

						@Override
						public void onError(String e) {
								if (userAccountFieldsCallback != null) {
										userAccountFieldsCallback.onError(e);
								}
						}
				});
				mNetworkUtils.request(url, "POST", body);
		}

		private void sendBackResults(GlobalProperties globalProperties) {
				if (userAccountFieldsCallback != null) {
						userAccountFieldsCallback.onUserSteem(userInfo.getUser().getBalance());
						userAccountFieldsCallback.onUserSteemDollar(userInfo.getUser().getSbd_balance());
						userAccountFieldsCallback.onUserSavingSteem(userInfo.getUser().getSavings_balance());
						userAccountFieldsCallback.onUserSavingSBD(userInfo.getUser().getSavings_sbd_balance());
						userAccountFieldsCallback.onUsdRates(sbd_usd, steem_usd);
						double sp = calculateSteemPower(globalProperties);
						userAccountFieldsCallback.onUserSteemPower(String.format(Locale.US, "%.2f SP", sp));
						userAccountFieldsCallback.onUserEstimatedAccountValue(String.format(Locale.US, "$ %.2f", estimateAccountValue(sp)));
				}
		}

		private double calculateSteemPower(GlobalProperties globalProperties) {
				double totalVestingShares = Double.valueOf(globalProperties.getResult().getTotal_vesting_shares().split(" ")[0]);
				double totalVestingFundSteem = Double.valueOf(globalProperties.getResult().getTotal_vesting_fund_steem().split(" ")[0]);
				double userVestingShare = Double.valueOf(userInfo.getUser().getVesting_share().split(" ")[0]);
				return ((totalVestingFundSteem * userVestingShare) / totalVestingShares);
		}

		private double estimateAccountValue(double sp) {
				double value = sbd_usd * Double.valueOf(userInfo.getUser().getSbd_balance().split(" ")[0]);
				value += steem_usd * sp;
				return value;
		}

		private UserAccountFieldsCallback userAccountFieldsCallback;

		public void setUserAccountFieldsCallback(UserAccountFieldsCallback userAccountFieldsCallback) {
				this.userAccountFieldsCallback = userAccountFieldsCallback;
		}

		public interface UserAccountFieldsCallback {
				void onUserSteem(String steem);

				void onUserSteemDollar(String dollar);

				void onUserSteemPower(String steemPower);

				void onUserSavingSteem(String savingSteem);

				void onUserSavingSBD(String savingSBD);

				void onUserEstimatedAccountValue(String value);

				void onUsdRates(double sbd_usd, double steem_usd);

				void onError(String error);
		}
}
