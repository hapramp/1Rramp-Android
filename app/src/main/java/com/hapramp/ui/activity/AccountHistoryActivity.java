package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.TransferHistoryCallback;
import com.hapramp.notification.FirebaseNotificationStore;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.ui.adapters.AccountHistoryAdapter;
import com.hapramp.utils.AccountHistoryItemDecoration;
import com.hapramp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountHistoryActivity extends AppCompatActivity implements TransferHistoryCallback {

  public static final String EXTRA_USERNAME = "username";
  @BindView(R.id.backBtn)
  ImageView closeBtn;
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
  private DataStore dataStore;
  private String mUsername;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_history);
    ButterKnife.bind(this);
    init();
    if (getIntent().getExtras() != null) {
      mUsername = getIntent().getExtras().getString(EXTRA_USERNAME, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
      String notifId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_NOTIFICATION_ID, null);
      toolbarTitle.setText(String.format("%s's Account History", mUsername));
      fetchHistory(mUsername);
      if (notifId != null) {
        FirebaseNotificationStore.markAsRead(notifId);
      }
    }
  }

  private void init() {
    dataStore = new DataStore();
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
   dataStore.requestTransferHistory(username,this);
  }

  private void bindData(ArrayList<TransferHistoryModel> transferHistory) {
    if (loadingProgressBar != null) {
      loadingProgressBar.setVisibility(View.GONE);
    }
    if (transferHistory.size() > 0) {
      emptyMessage.setVisibility(View.GONE);
      Collections.reverse(transferHistory);
      accountHistoryAdapter.setTransferHistoryModels(transferHistory);
    } else {
      emptyMessage.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onAccountTransferHistoryAvailable(ArrayList<TransferHistoryModel> history) {
    bindData(history);
  }

  @Override
  public void onAccountTransferHistoryError(String error) {

  }
}
