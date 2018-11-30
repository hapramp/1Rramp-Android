package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CompetitionEntriesFetchCallback;
import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.WinnersFeedListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnersFeedListActivity extends AppCompatActivity implements CompetitionEntriesFetchCallback {
  public static final String EXTRA_COMPETITION_ID = "competition_id";
  public static final String EXTRA_COMPETITION_TITLE = "competition_title";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.winners_list)
  RecyclerView winnersList;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  @BindView(R.id.competition_title)
  TextView competitionTitle;
  @BindView(R.id.competition_title_container)
  LinearLayout competitionTitleContainer;
  private WinnersFeedListAdapter winnersFeedListAdapter;
  private DataStore dataStore;
  private String mCompetitionId;
  private String mCompetitionTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_winners_feed_list);
    ButterKnife.bind(this);
    collectExtras();
    initializeList();
    attachListeners();
    fetchWinners();
  }

  private void collectExtras() {
    try {
      Intent intent = getIntent();
      mCompetitionId = intent.getStringExtra(EXTRA_COMPETITION_ID);
      mCompetitionTitle = intent.getStringExtra(EXTRA_COMPETITION_TITLE);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initializeList() {
    dataStore = new DataStore();
    winnersFeedListAdapter = new WinnersFeedListAdapter(this);
    winnersList.setLayoutManager(new LinearLayoutManager(this));
    winnersList.setAdapter(winnersFeedListAdapter);
    competitionTitle.setText("Winners List For :" + mCompetitionTitle);
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void fetchWinners() {
    dataStore.requestWinnersList(mCompetitionId, this);
  }

  @Override
  public void onCompetitionsEntriesAvailable(List<Feed> entries) {
    showProgress(false);
    if (entries.size() > 0) {
      showMessage(false, "");
      marshellAndSetEntriesToList(entries);
    } else {
      showMessage(true, "No winners available");
    }
  }

  @Override
  public void onCompetitionsEntriesFetchError() {
    showMessage(true, "Failed to get winners list.");
    showProgress(false);
  }

  private void showProgress(boolean show) {
    if (loadingProgressBar != null) {
      if (show) {
        loadingProgressBar.setVisibility(View.VISIBLE);
      } else {
        loadingProgressBar.setVisibility(View.GONE);
      }
    }
  }

  private void showMessage(boolean show, String msg) {
    if (messagePanel != null) {
      if (show) {
        messagePanel.setText(msg);
        messagePanel.setVisibility(View.VISIBLE);
      } else {
        messagePanel.setVisibility(View.GONE);
      }
    }
  }

  private void marshellAndSetEntriesToList(List<Feed> entries) {
    ArrayList<RankableCompetitionFeedItem> rankableItems = new ArrayList<>();
    for (int i = 0; i < entries.size(); i++) {
      rankableItems.add(new RankableCompetitionFeedItem(entries.get(i)));
    }
    winnersFeedListAdapter.setWinnerFeedItems(rankableItems);
  }
}
