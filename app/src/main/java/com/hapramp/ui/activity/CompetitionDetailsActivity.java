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
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.CompetitionDetailsRecyclerAdapter;
import com.hapramp.utils.CountDownTimerUtils;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PrizeMoneyFilter;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_HASHTAG;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_ID;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_TITLE;
import static com.hapramp.ui.activity.WinnerDeclarationActivity.EXTRA_COMPETITION_IMAGE_URL;
import static com.hapramp.ui.activity.WinnerDeclarationActivity.EXTRA_COMPETITION_TAGS;

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
  @BindView(R.id.prize_amount)
  TextView prizeAmount;
  @BindView(R.id.actionButton)
  TextView actionButton;
  private CountDownTimerUtils countDownTimerUtils;

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
    countDownTimerUtils = new CountDownTimerUtils();
    dataStore = new DataStore();
    if (headerData != null) {
      showMessagePanel(false, "");
      setPrizeAmount(PrizeMoneyFilter.getTotalPrize(headerData.getPrizes()));
      invalidateActionButton();
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

  private void setPrizeAmount(String amount) {
    prizeAmount.setText(amount);
  }

  private void invalidateActionButton() {
    String start_time = headerData.getmStartsAt();
    String end_time = headerData.getmEndsAt();
    long now = System.currentTimeMillis();
    long endsAt = MomentsUtils.getMillisFromTime(end_time);
    long startsAt = MomentsUtils.getMillisFromTime(start_time);
    if (!headerData.isWinners_announced()) {
      //not started
      if (now < startsAt) {
        setWhenNotStarted();
      }//running/live
      else if (now > startsAt && now < endsAt) {
        setWhenRunning(isAdmin());
      }
      //ended
      else if (now > endsAt) {
        setWhenEndedButWinnersNotDeclared(isAdmin());
      }
    } else {
      setWhenWinnerAnnounced();
    }
  }

  private void setAdapter() {
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setSkipPostitions(0);
    viewItemDecoration.setWantTopOffset(false, 0);
    competitionDetailsList.addItemDecoration(viewItemDecoration);
    competitionDetailsRecyclerAdapter = new CompetitionDetailsRecyclerAdapter(this, headerData);
    competitionDetailsList.setLayoutManager(new LinearLayoutManager(this));
    competitionDetailsList.setAdapter(competitionDetailsRecyclerAdapter);
  }

  private void setWhenNotStarted() {
    if (actionButton != null) {
      actionButton.setVisibility(View.GONE);
    }
  }

  private void setWhenRunning(boolean admin) {
    if (admin) {
      //gone
      if (actionButton != null) {
        actionButton.setVisibility(View.GONE);
      }
    } else {
      if (actionButton != null) {
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setEnabled(true);
        actionButton.setClickable(true);
        actionButton.setText("SUBMIT ENTRY");
        actionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            openSubmissionPage();
          }
        });
      }
    }
  }

  private boolean isAdmin() {
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    return headerData.getmAdmin().getmUsername().equals(username);
  }

  private void setWhenEndedButWinnersNotDeclared(boolean admin) {
    if (admin) {
      if (actionButton != null) {
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setEnabled(true);
        actionButton.setClickable(true);
        actionButton.setText("DECLARE WINNER");
        actionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            openResultDeclarationPage();
          }
        });
      }
    } else {
      if (actionButton != null) {
        actionButton.setVisibility(View.GONE);
      }
    }
  }

  private void setWhenWinnerAnnounced() {
    if (actionButton != null) {
      actionButton.setVisibility(View.VISIBLE);
      actionButton.setEnabled(true);
      actionButton.setClickable(true);
      actionButton.setText("VIEW WINNER");
      actionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          openWinnersList();
        }
      });
    }
  }

  private void openSubmissionPage() {
    Intent intent = new Intent(this, ParticipateEditorActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, headerData.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, headerData.getmTitle());
    intent.putExtra(EXTRA_COMPETITION_HASHTAG, headerData.getmParticipationHashtag());
    startActivity(intent);
    overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
  }

  private void openResultDeclarationPage() {
    Intent intent = new Intent(this, WinnerDeclarationActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, headerData.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, headerData.getmTitle());
    intent.putExtra(EXTRA_COMPETITION_IMAGE_URL, headerData.getmImage());
    intent.putParcelableArrayListExtra(EXTRA_COMPETITION_TAGS, headerData.getCommunities());
    startActivity(intent);
    finish();
  }

  private void openWinnersList() {
    Intent intent = new Intent(this, WinnersFeedListActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, headerData.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, headerData.getmTitle());
    startActivity(intent);
  }

  @Override
  public void onCompetitionsEntriesAvailable(List<Feed> entries) {
    try {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
      invalidateActionButton();
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

  private void setEndsInTimer() {
    long now = System.currentTimeMillis();
    long ends = MomentsUtils.getMillisFromTime(headerData.getmEndsAt());
    long left = ends - now;
    countDownTimerUtils.setTimerWith(left, 1000, new CountDownTimerUtils.TimerUpdateListener() {
      @Override
      public void onFinished() {
        invalidateActionButton();
      }

      @Override
      public void onRunningTimeUpdate(String updateTime) {
      }
    });
    countDownTimerUtils.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (countDownTimerUtils != null) {
      countDownTimerUtils.cancel();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    invalidateTimers();
    fetchEntries();
  }

  private void setStartsInTimer() {
    long now = System.currentTimeMillis();
    long starts = MomentsUtils.getMillisFromTime(headerData.getmStartsAt());
    long left = starts - now;
    countDownTimerUtils.setTimerWith(left, 1000, new CountDownTimerUtils.TimerUpdateListener() {
      @Override
      public void onFinished() {
        invalidateActionButton();
      }

      @Override
      public void onRunningTimeUpdate(String updateTime) {
      }
    });
    countDownTimerUtils.start();
  }

  private void invalidateTimers() {
    String start_time = headerData.getmStartsAt();
    String end_time = headerData.getmEndsAt();
    long now = System.currentTimeMillis();
    long endsAt = MomentsUtils.getMillisFromTime(end_time);
    long startsAt = MomentsUtils.getMillisFromTime(start_time);
    //not started
    if (now < startsAt) {
      setStartsInTimer();
    }//running/live
    else if (now > startsAt && now < endsAt) {
      setEndsInTimer();
    }
  }
}
