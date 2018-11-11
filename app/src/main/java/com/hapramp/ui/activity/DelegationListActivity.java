package com.hapramp.ui.activity;

import android.content.Intent;
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
import com.hapramp.datastore.callbacks.DelegationsCallback;
import com.hapramp.models.DelegationModel;
import com.hapramp.ui.adapters.DelegationListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelegationListActivity extends AppCompatActivity implements DelegationsCallback {
  public static final String EXTRA_KEY_DELEGATOR = "delegator";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.delegation_rv)
  RecyclerView delegationsRv;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  private String mUsername;
  private DataStore dataStore;
  private DelegationListAdapter delegationListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delegation_list);
    ButterKnife.bind(this);
    initializeObjects();
    attachListeners();
    collectExtras();
    fetchDelegations();
  }


  private void initializeObjects() {
    dataStore = new DataStore();
    delegationListAdapter = new DelegationListAdapter();
    delegationsRv.setLayoutManager(new LinearLayoutManager(this));
    delegationsRv.setAdapter(delegationListAdapter);
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void collectExtras() {
    Intent ri = getIntent();
    if (ri != null) {
      mUsername = ri.getStringExtra(EXTRA_KEY_DELEGATOR);
      actionBarTitle.setText(String.format("%s's Delegations", mUsername));
    }
  }

  private void fetchDelegations() {
    if (mUsername != null) {
      dataStore.requestDelegations(mUsername, this);
    } else {
      showProgressBar(false);
      showMessage(true, "Delegator not present!");
    }
  }

  private void showProgressBar(boolean show) {
    if (loadingProgressBar != null) {
      int vis = show ? View.VISIBLE : View.GONE;
      loadingProgressBar.setVisibility(vis);
    }
  }

  private void showMessage(boolean show, String msg) {
    if (messagePanel != null) {
      if (show) {
        messagePanel.setVisibility(View.VISIBLE);
        messagePanel.setText(msg);
      } else {
        messagePanel.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onDelegationsFetched(ArrayList<DelegationModel> delegationModels) {
    showProgressBar(false);
    if (delegationModels.size() > 0) {
      delegationListAdapter.setDelegationModels(delegationModels);
      showMessage(false, "");
    } else {
      showMessage(true, mUsername + " haven't delegated yet!");
    }
  }

  @Override
  public void onDelegationsFetchFailed() {
    showMessage(true, "Failed to fetch delegations!");
    showProgressBar(false);
  }
}
