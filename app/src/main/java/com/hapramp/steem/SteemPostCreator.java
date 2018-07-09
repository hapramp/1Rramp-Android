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
import com.hapramp.ui.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

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
