package com.hapramp.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.hapramp.ui.adapters.CompetitionsListRecyclerAdapter;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.competition.CompetitionFeedItemView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CompetitionFragment extends Fragment implements CompetitionsListCallback, CompetitionFeedItemView.CompetitionItemDeleteListener {

  @BindView(R.id.competition_list)
  RecyclerView competitionList;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  Unbinder unbinder;

  CompetitionsListRecyclerAdapter competitionsListRecyclerAdapter;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;
  private Context mContext;
  private DataStore dataStore;

  public CompetitionFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    this.mContext = context;
    super.onAttach(context);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_competition, container, false);
    unbinder = ButterKnife.bind(this, view);
    initializeList();
    fetchCompetitionsList();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    fetchCompetitionsList();
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
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void initializeList() {
    dataStore = new DataStore();
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    competitionsListRecyclerAdapter = new CompetitionsListRecyclerAdapter(mContext);
    competitionsListRecyclerAdapter.setDeleteListener(this);
    competitionList.setLayoutManager(new LinearLayoutManager(mContext));
    competitionList.addItemDecoration(viewItemDecoration);
    competitionList.setAdapter(competitionsListRecyclerAdapter);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fetchCompetitionsList();
      }
    });
  }

  @Override
  public void onCompetitionsListAvailable(List<CompetitionModel> competitions) {
    try {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
    }
    catch (Exception e) {

    }
    setProgressVisibility(false);
    if (competitions != null) {
      if (competitions.size() == 0) {
        setMessagePanel(true, "No competitions running!");
      } else {
        setMessagePanel(false, "");
        competitionsListRecyclerAdapter.setCompetitions(competitions);
      }
    } else {
      setMessagePanel(true, "Something went wrong!");
    }
  }

  @Override
  public void onCompetitionsFetchError() {
    try {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
      setProgressVisibility(false);
      setMessagePanel(true, "Something went wrong!");
    }
    catch (Exception e) {

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
  public void onCompetitionItemDeleted() {
    fetchCompetitionsList();
  }
}
