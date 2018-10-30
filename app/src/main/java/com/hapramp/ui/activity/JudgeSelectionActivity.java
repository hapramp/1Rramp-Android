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
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.JudgesListFetchFromServerCallback;
import com.hapramp.models.JudgeModel;
import com.hapramp.ui.adapters.JudgeListAdapter;
import com.hapramp.views.JudgeRemovableItemView;

import java.util.ArrayList;

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
  @BindView(R.id.selected_judge_container)
  LinearLayout selectedJudgeContainer;
  @BindView(R.id.done_btn)
  TextView doneBtn;
  @BindView(R.id.selected_judge_wrapper)
  RelativeLayout selectedJudgeWrapper;
  @BindView(R.id.shadow)
  View shadow;
  private DataStore dataStore;
  private JudgeListAdapter judgeListAdapter;
  private ArrayList<JudgeModel> selectedJudges;
  private ArrayList<JudgeModel> mAllJudges;

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

    doneBtn.setOnClickListener(new View.OnClickListener() {
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
  }

  @Override
  public void onJudgesListAvailable(ArrayList<JudgeModel> judgeList) {
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
    updateJudgesSelectionInList(judgeList);
    updateBottomBarView();
  }

  @Override
  public void onJudgesFetchError(String msg) {

  }

  private void updateJudgesSelectionInList(ArrayList<JudgeModel> judgeList) {
    this.mAllJudges = judgeList;
    for (int i = 0; i < judgeList.size(); i++) {
      boolean found = false;
      for (int j = 0; j < selectedJudges.size(); j++) {
        if (judgeList.get(i).getmId() == selectedJudges.get(j).getmId()) {
          found = true;
          break;
        }
      }
      if (found) {
        judgeList.get(i).setSelected(true);
      } else {
        judgeList.get(i).setSelected(false);
      }
    }
    judgeListAdapter.setJudges(judgeList);
  }

  @Override
  public void onAddJudge(JudgeModel judge) {
    addJudge(judge);
    updateBottomBarView();
  }

  @Override
  public void onRemoveJudge(JudgeModel judge) {
    removeJudge(judge);
    updateJudgesSelectionInList(mAllJudges);
    updateBottomBarView();
  }

  private void addJudge(JudgeModel judge) {
    if (selectedJudges.size() < MAX_JUDGES_ALLOWED) {
      selectedJudges.add(judge);
      judge.setSelected(true);
      updateJudgesSelectionInList(mAllJudges);
    } else {
      Toast.makeText(this, "Max " + MAX_JUDGES_ALLOWED + " judges allowed!", Toast.LENGTH_LONG).show();
    }
  }

  private void updateBottomBarView() {
    try {
      final int selected = selectedJudges.size();
      if (selected > 0) {
        //show bottom bar
        shadow.setVisibility(View.VISIBLE);
        selectedJudgeWrapper.setVisibility(View.VISIBLE);
        selectedJudgeContainer.removeAllViews();
        for (int i = 0; i < selected; i++) {
          JudgeRemovableItemView judgeRemovableItemView = new JudgeRemovableItemView(this);
          judgeRemovableItemView.setmJudgeRemoveListener(new JudgeRemovableItemView.JudgeRemoveListener() {
            @Override
            public void onRemoveJudge(JudgeModel judgeModel) {
              onJudgeRemovedFromBottomBarAt(judgeModel);
            }
          });
          judgeRemovableItemView.setJudge(selectedJudges.get(i), i);
          selectedJudgeContainer.addView(judgeRemovableItemView, i);
        }
      } else {
        //hide bottom bar
        shadow.setVisibility(View.GONE);
        selectedJudgeWrapper.setVisibility(View.GONE);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void onJudgeRemovedFromBottomBarAt(JudgeModel judgeModel) {
    removeJudge(judgeModel);
    updateJudgesSelectionInList(mAllJudges);
    updateBottomBarView();
  }

  private void removeJudge(JudgeModel judgeModel) {
    for (int i = 0; i < selectedJudges.size(); i++) {
      if (selectedJudges.get(i).getmId() == judgeModel.getmId()) {
        selectedJudges.remove(i);
      }
    }
  }
}
