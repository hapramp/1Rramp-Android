package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CompetitionModel;
import com.hapramp.ui.adapters.CompetitionDetailsRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionDetailsActivity extends AppCompatActivity {

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_competition_details);
    ButterKnife.bind(this);
    collectHeaderParcel();
    initializeList();
    attachListeners();
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void collectHeaderParcel() {
    Intent i = getIntent();
    if (i != null) {
      headerData = i.getParcelableExtra(EXTRA_HEADER_PARCEL);
    }
  }

  private void initializeList() {
    if (headerData != null) {
      showMessagePanel(false, "");
      competitionDetailsRecyclerAdapter = new CompetitionDetailsRecyclerAdapter(this, headerData);
      competitionDetailsList.setLayoutManager(new LinearLayoutManager(this));
      competitionDetailsList.setAdapter(competitionDetailsRecyclerAdapter);
    } else {
      showMessagePanel(true, "Invalid competition data!");
    }
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
}
