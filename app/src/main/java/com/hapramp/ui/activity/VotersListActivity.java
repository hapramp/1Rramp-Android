package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.VoterData;
import com.hapramp.steem.models.Voter;
import com.hapramp.ui.adapters.VoterListAdapter;
import com.hapramp.utils.ReputationCalc;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VotersListActivity extends AppCompatActivity {

  public static final String EXTRA_USER_LIST = "extra_user_list";
  public String quote;
  public String base;
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.voters_recyclerview)
  RecyclerView userListRecyclerView;
  private ArrayList<Voter> voters;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voters_list);
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
    voters = getIntent().getParcelableArrayListExtra(EXTRA_USER_LIST);
    userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    VoterListAdapter voterListAdapter = new VoterListAdapter();
    userListRecyclerView.setAdapter(voterListAdapter);
    voterListAdapter.setVotersData(processVoterData(voters));
  }

  private ArrayList<VoterData> processVoterData(ArrayList<Voter> voters) {
    ArrayList<VoterData> processedData = new ArrayList<>();
    for (int i = 0; i < voters.size(); i++) {
      processedData.add(new VoterData(voters.get(i).getVoter(),
        String.format(Locale.US, "%d%%", voters.get(i).getPercent() / 100),
        String.format(Locale.US, "(%.2f)", ReputationCalc.calculateReputation(Long.valueOf(voters.get(i).getReputation())))));
    }
    return processedData;
  }
}
