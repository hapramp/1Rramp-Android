package com.hapramp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.LeaderboardModel;
import com.hapramp.models.VoterData;
import com.hapramp.steem.models.Voter;
import com.hapramp.ui.adapters.LeaderboardAdapter;
import com.hapramp.ui.adapters.VoterListAdapter;
import com.hapramp.utils.ReputationCalc;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardActivity extends AppCompatActivity {
  public static final String EXTRA_LEADERBOARD = "leaderboard.activity";

  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.leaderboard_recyclerview)
  RecyclerView leaderboardRecyclerview;
  private ArrayList<LeaderboardModel.Winners> leaders;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaderboard_list);
    ButterKnife.bind(this);
    attachListeners();
    initList();
  }


  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void initList() {
    leaders = getIntent().getParcelableArrayListExtra(EXTRA_LEADERBOARD);
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.leaderboard_item_divider);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    leaderboardRecyclerview.addItemDecoration(viewItemDecoration);
    leaderboardRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    LeaderboardAdapter adapter = new LeaderboardAdapter();
    leaderboardRecyclerview.setAdapter(adapter);
    adapter.setLeaders(leaders);
  }
}
