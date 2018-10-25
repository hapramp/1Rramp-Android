package com.hapramp.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CompetitionsListCallback;
import com.hapramp.models.CompetitionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.CompetitionsListRecyclerAdapter;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCompetitionsFragment extends Fragment implements CompetitionsListCallback {
  Context mContext;
  Unbinder unbinder;
  @BindView(R.id.competition_list)
  RecyclerView competitionList;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  private DataStore dataStore;
  private CompetitionsListRecyclerAdapter competitionsListRecyclerAdapter;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_competitions_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    initializeList();
    fetchCompetitionsList();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void initializeList() {
    dataStore = new DataStore();
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    competitionsListRecyclerAdapter = new CompetitionsListRecyclerAdapter(mContext);
    competitionList.setLayoutManager(new LinearLayoutManager(mContext));
    competitionList.addItemDecoration(viewItemDecoration);
    competitionList.setAdapter(competitionsListRecyclerAdapter);
  }

  private void fetchCompetitionsList() {
    setProgressVisibility(true);
    dataStore.requestCompetitionLists(this);
  }

  private void setProgressVisibility(boolean show) {
    if (loadingProgressBar != null) {
      if (show) {
        loadingProgressBar.setVisibility(View.VISIBLE);
      } else {
        loadingProgressBar.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onCompetitionsListAvailable(List<CompetitionModel> competitions) {
    filterMyCompetitions(competitions);
    setProgressVisibility(false);
    if (competitions != null) {
      if (competitions.size() == 0) {
        setMessagePanel(true, "You have not created any competitions yet!");
      } else {
        setMessagePanel(false, "");
        competitionsListRecyclerAdapter.setCompetitions(competitions);
      }
    } else {
      setMessagePanel(true, "Something went wrong!");
    }
  }

  private void filterMyCompetitions(List<CompetitionModel> competitions) {
    String myUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    for (int i = 0; i < competitions.size(); i++) {
      if (!competitions.get(i).getmAdmin().getmUsername().equals(myUsername)) {
        competitions.remove(i);
      }
    }
  }

  private void setMessagePanel(boolean show, String msg) {
    if (messagePanel != null) {
      if (show) {
        messagePanel.setText(msg);
        messagePanel.setVisibility(View.VISIBLE);
      } else {
        messagePanel.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onCompetitionsFetchError() {
    setProgressVisibility(false);
    setMessagePanel(true, "Something went wrong!");
  }
}
