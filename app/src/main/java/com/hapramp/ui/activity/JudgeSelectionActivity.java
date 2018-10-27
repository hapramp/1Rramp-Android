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
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.JudgesListFetchFromServerCallback;
import com.hapramp.models.JudgeModel;
import com.hapramp.ui.adapters.JudgeListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.views.JudgeSelectionView.MAX_JUDGES_ALLOWED;

public class JudgeSelectionActivity extends AppCompatActivity implements JudgesListFetchFromServerCallback, JudgeListAdapter.JudgeListListener {
  public static final String EXTRA_SELECTED_JUDGES = "selected_judges";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.judges_list)
  RecyclerView judgesList;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  private DataStore dataStore;
  private JudgeListAdapter judgeListAdapter;
  private ArrayList<JudgeModel> selectedJudges;
  private List<JudgeModel> mAllJudges;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_judge_selection);
    ButterKnife.bind(this);
    collectAlreadySelectedJudges();
    init();
    attachListeners();
    fetchJudges();
  }

  private void collectAlreadySelectedJudges() {
    selectedJudges = new ArrayList<>();
    if (getIntent() != null) {
      selectedJudges = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_JUDGES);
    }
  }

  private void init() {
    judgeListAdapter = new JudgeListAdapter(this);
    judgeListAdapter.setJudgeListListener(this);
    judgesList.setLayoutManager(new LinearLayoutManager(this));
    judgesList.setAdapter(judgeListAdapter);
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        returnResult();
      }
    });
  }

  private void fetchJudges() {
    dataStore = new DataStore();
    dataStore.requestsJudges(this);
  }

  private void returnResult() {
    Intent intent = new Intent();
    intent.putParcelableArrayListExtra(EXTRA_SELECTED_JUDGES, selectedJudges);
    setResult(RESULT_OK, intent);
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  @Override
  public void onJudgesListAvailable(List<JudgeModel> judgeList) {
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
    manipulateSelection(judgeList);
  }

  private void manipulateSelection(List<JudgeModel> judgeList) {
    this.mAllJudges = judgeList;
    for (int i = 0; i < selectedJudges.size(); i++) {
      for (int j = 0; j < judgeList.size(); j++) {
        if (judgeList.get(j).getmId() == selectedJudges.get(i).getmId()) {
          judgeList.get(j).setSelected(true);
        }
      }
    }
    judgeListAdapter.setJudges((ArrayList<JudgeModel>) judgeList);
  }

  @Override
  public void onJudgesFetchError(String msg) {

  }

  @Override
  public void onAddJudge(JudgeModel judge) {
    addJudge(judge);
  }

  @Override
  public void onRemoveJudge(JudgeModel judge) {
    removeJudge(judge);
    manipulateSelection(mAllJudges);
  }

  private void removeJudge(JudgeModel judgeModel) {
    for (int i = 0; i < selectedJudges.size(); i++) {
      if (selectedJudges.get(i).getmId() == judgeModel.getmId()) {
        selectedJudges.remove(i);
        judgeModel.setSelected(false);
      }
    }
  }

  private void addJudge(JudgeModel judge) {
    if (selectedJudges.size() < MAX_JUDGES_ALLOWED) {
      selectedJudges.add(judge);
      judge.setSelected(true);
      manipulateSelection(mAllJudges);
    } else {
      Toast.makeText(this, "Max " + MAX_JUDGES_ALLOWED + " judges allowed!", Toast.LENGTH_LONG).show();
    }
  }
}
