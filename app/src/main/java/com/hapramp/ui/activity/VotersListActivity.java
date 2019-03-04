package com.hapramp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.VoterData;
import com.hapramp.steem.models.Voter;
import com.hapramp.ui.adapters.VoterListAdapter;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VotersListActivity extends AppCompatActivity {
  public static final String EXTRA_USER_LIST = "extra_user_list";
  public static final String EXTRA_TOTAL_EARNING = "total_earning";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.voters_recyclerview)
  RecyclerView userListRecyclerView;
  private ArrayList<Voter> voters;
  private double totalEarning = 0;
  private Comparator<? super VoterData> votersComparator = (Comparator<VoterData>) (voter1, voter2) -> {
    int comp = voter1.getRshare() > voter2.getRshare() ? -1 : 1;
    return comp;
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voters_list);
    ButterKnife.bind(this);
    attachListeners();
    initList();
  }

  private void attachListeners() {
    backBtn.setOnClickListener(view -> finish());
  }

  private void initList() {
    totalEarning = getIntent().getDoubleExtra(EXTRA_TOTAL_EARNING, 0);
    voters = getIntent().getParcelableArrayListExtra(EXTRA_USER_LIST);
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.leaderboard_item_divider);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    userListRecyclerView.addItemDecoration(viewItemDecoration);
    userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    VoterListAdapter voterListAdapter = new VoterListAdapter();
    userListRecyclerView.setAdapter(voterListAdapter);
    voterListAdapter.setVotersData(processVoterData(voters));
  }

  private ArrayList<VoterData> processVoterData(ArrayList<Voter> voters) {
    ArrayList<VoterData> processedData = new ArrayList<>();
    try {
      long totalRshares = 0;
      for (int i = 0; i < voters.size(); i++) {
        totalRshares += voters.get(i).getRshare();
      }
      for (int i = 0; i < voters.size(); i++) {
        double percent = voters.get(i).getPercent();
        long rshare = voters.get(i).getRshare();
        double voteValue = calculateVoteValueFrom(rshare, totalRshares, totalEarning);
        voteValue = Double.parseDouble(String.format(Locale.US, "%.3f", voteValue));
        String voteValueString = voteValue > 0 ? String.format(Locale.US, "$%.3f", voteValue) : "";
        processedData.add(new VoterData(voters.get(i).getVoter(),
          String.format(Locale.US, String.format(Locale.US, "%.2f", percent / 100)) + "%",
          rshare, voteValueString));
      }
      Collections.sort(processedData, votersComparator);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return processedData;
  }

  private double calculateVoteValueFrom(double rshare, long totalRshares, double totalEarning) {
    return (totalEarning * rshare) / totalRshares;
  }
}
