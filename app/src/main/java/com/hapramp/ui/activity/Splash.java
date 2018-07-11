package com.hapramp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.search.TranserHistoryManager;
import com.hapramp.steem.TransferHistoryParser;
import com.hapramp.steem.models.TransferHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				testRPC();
				if (HaprampPreferenceManager.getInstance().isOnBoardingDone()) {
						navigateToLogin();
				} else {
						navigateToOnBoarding();
				}
		}

		private void navigateToLogin() {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
		}

		private void navigateToOnBoarding() {
				Intent intent = new Intent(this, OnBoardingActivity.class);
				startActivity(intent);
				finish();
		}

		public void testRPC() {

		}
}