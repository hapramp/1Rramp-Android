package com.hapramp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.search.TranserHistoryManager;
import com.hapramp.steem.TransferHistoryParser;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.ui.adapters.AccountHistoryAdapter;
import com.hapramp.utils.AccountHistoryItemDecoration;
import com.hapramp.utils.FontManager;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountHistoryActivity extends AppCompatActivity {

		@BindView(R.id.closeBtn)
		TextView closeBtn;
		@BindView(R.id.toolbar_container)
		RelativeLayout toolbarContainer;
		@BindView(R.id.accountHistoryRv)
		RecyclerView accountHistoryRv;
		@BindView(R.id.loadingProgressBar)
		ProgressBar loadingProgressBar;
		@BindView(R.id.toolbar_title)
		TextView toolbarTitle;
		@BindView(R.id.empty_message)
		TextView emptyMessage;

		private AccountHistoryAdapter accountHistoryAdapter;
		public static final String EXTRA_USERNAME = "username";
		private Handler mHandler;
		private String mUsername;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_account_history);
				ButterKnife.bind(this);
				init();
				if (getIntent().getExtras() != null) {
				mUsername = getIntent().getExtras().getString(EXTRA_USERNAME, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
				toolbarTitle.setText(String.format("%s`s Account History", mUsername));
				fetchHistory(mUsername);
				}
		}

		private void init() {
				mHandler = new Handler();
				closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
				accountHistoryAdapter = new AccountHistoryAdapter(this);
				accountHistoryRv.setLayoutManager(new LinearLayoutManager(this));
				AccountHistoryItemDecoration itemDecoration = new AccountHistoryItemDecoration();
				accountHistoryRv.addItemDecoration(itemDecoration);
				accountHistoryRv.setAdapter(accountHistoryAdapter);
				closeBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
								finish();
						}
				});
		}

		private void fetchHistory(final String username) {
				final TransferHistoryParser transferHistoryParser = new TransferHistoryParser();
				final TranserHistoryManager transerHistoryManager = new TranserHistoryManager();
				transerHistoryManager.setTransferHistoryCallback(new TranserHistoryManager.TransferHistoryCallback() {
						@Override
						public void onRawTransferResponse(final String response) {
								mHandler.post(new Runnable() {
										@Override
										public void run() {
												bindData(transferHistoryParser.parseTransferHistory(response, username));
										}
								});
						}

						@Override
						public void onRawTransferResponseError(String e) {

						}
				});
				transerHistoryManager.requestRawTransferHistory(username);
		}

		private void bindData(ArrayList<TransferHistoryModel> transferHistory) {
				if (loadingProgressBar != null) {
						loadingProgressBar.setVisibility(View.GONE);
				}
				if(transferHistory.size() > 0) {
						emptyMessage.setVisibility(View.GONE);
						Collections.reverse(transferHistory);
						accountHistoryAdapter.setTransferHistoryModels(transferHistory);
				}else{
						emptyMessage.setVisibility(View.VISIBLE);
				}
		}
}
