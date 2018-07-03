package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.data.Content;
import com.hapramp.steem.models.data.JsonMetadata;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.steemconnect4j.StringUtils;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BeneficiaryRouteType;
import eu.bittrade.libs.steemj.base.models.CommentOptionsExtension;
import eu.bittrade.libs.steemj.base.models.CommentPayoutBeneficiaries;
import eu.bittrade.libs.steemj.base.models.DynamicGlobalProperty;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.base.models.operations.CommentOptionsOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
	* Created by Ankit on 2/21/2018.
	*/

public class SteemPostCreator {
		private Handler mHandler;

		public SteemPostCreator() {
				this.mHandler = new Handler();
		}

		@WorkerThread
		public void createPost(final String body, final String title, final List<String> tags, final Content content, final String __permlink) {
				new Thread() {
						@Override
						public void run() {
								Log.d("PostCreate",HaprampPreferenceManager.getInstance()
										.getSC2AccessToken());
								SteemConnect steemConnect = SteemConnectUtils
										.getSteemConnectInstance(HaprampPreferenceManager.getInstance()
												.getSC2AccessToken());
								String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
								String jsonMetadata = new JsonMetadata(tags, content).getJson();
								steemConnect.comment("",
										LocalConfig.PARENT_PERMALINK,
										username,
										__permlink,
										com.hapramp.utils.StringUtils.stringify(title),
										com.hapramp.utils.StringUtils.stringify(body),
										com.hapramp.utils.StringUtils.stringify(jsonMetadata),
										new SteemConnectCallback() {
												@Override
												public void onResponse(String s) {
														if(steemPostCreatorCallback != null) {
																mHandler.post(new Runnable() {
																		@Override
																		public void run() {
																				steemPostCreatorCallback.onPostCreatedOnSteem();
																		}
																});
														}
												}

												@Override
												public void onError(final SteemConnectException e) {
														if(steemPostCreatorCallback != null) {
																mHandler.post(new Runnable() {
																		@Override
																		public void run() {
																				steemPostCreatorCallback.onPostCreationFailedOnSteem(e.toString());
																		}
																});
														}
												}
										}
								);
						}
				}.start();
		}

		private SteemPostCreatorCallback steemPostCreatorCallback;

		public void setSteemPostCreatorCallback(SteemPostCreatorCallback steemPostCreatorCallback) {
				this.steemPostCreatorCallback = steemPostCreatorCallback;
		}

		public interface SteemPostCreatorCallback {
				void onPostCreatedOnSteem();

				void onPostCreationFailedOnSteem(String msg);
		}

}
