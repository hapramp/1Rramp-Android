package com.hapramp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CompetitionEntriesFetchCallback;
import com.hapramp.models.CompetitionModel;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.CompetitionDetailsRecyclerAdapter;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionDetailsActivity extends AppCompatActivity implements CompetitionEntriesFetchCallback {

  public static final String EXTRA_HEADER_PARCEL = "header_parcel";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.overflowBtn)
  ImageView overflowBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.competition_details_list)
  RecyclerView competitionDetailsList;
  CompetitionDetailsRecyclerAdapter competitionDetailsRecyclerAdapter;
  CompetitionModel headerData;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  DataStore dataStore;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_competition_details);
    ButterKnife.bind(this);
    collectHeaderParcel();
    initializeList();
    attachListeners();
    fetchEntries();
  }

  private void collectHeaderParcel() {
    Intent i = getIntent();
    if (i != null) {
      headerData = i.getParcelableExtra(EXTRA_HEADER_PARCEL);
    }
  }

  private void initializeList() {
    dataStore = new DataStore();
    if (headerData != null) {
      showMessagePanel(false, "");
      setAdapter();
      swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          setAdapter();
          fetchEntries();
        }
      });
    } else {
      showMessagePanel(true, "Invalid competition data!");
    }
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void fetchEntries() {
    dataStore.requestCompetitionEntries(headerData.getmId(), this);
  }

  private void showMessagePanel(boolean show, String msg) {
    if (messagePanel != null) {
      if (show) {
        messagePanel.setText(msg);
        messagePanel.setVisibility(View.VISIBLE);
      } else {
        messagePanel.setVisibility(View.GONE);
      }
    }
  }

  private void setAdapter() {
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    competitionDetailsList.addItemDecoration(viewItemDecoration);
    competitionDetailsRecyclerAdapter = new CompetitionDetailsRecyclerAdapter(this, headerData);
    competitionDetailsList.setLayoutManager(new LinearLayoutManager(this));
    competitionDetailsList.setAdapter(competitionDetailsRecyclerAdapter);
  }

  @Override
  public void onCompetitionsEntriesAvailable(List<Feed> entries) {
    try {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
      competitionDetailsRecyclerAdapter.addSubmissions(entries);
    }
    catch (Exception e) {

    }
  }

  @Override
  public void onCompetitionsEntriesFetchError() {
    try {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
      Toast.makeText(this, "Failed to fetch entries", Toast.LENGTH_LONG).show();
    }
    catch (Exception e) {

    }
  }
}
