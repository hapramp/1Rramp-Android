package com.hapramp.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.hapramp.models.CompetitionListResponse;
import com.hapramp.models.CompetitionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.CompetitionsListRecyclerAdapter;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.competition.CompetitionFeedItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCompetitionsFragment extends Fragment implements CompetitionsListCallback, CompetitionFeedItemView.CompetitionItemDeleteListener, CompetitionsListRecyclerAdapter.LoadMoreCompetitionsCallback {
  Context mContext;
  Unbinder unbinder;
  @BindView(R.id.competition_list)
  RecyclerView competitionList;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;
  private DataStore dataStore;
  private CompetitionsListRecyclerAdapter competitionsListRecyclerAdapter;

  private String lastCompetitionId = "";
  private String lastLoadedOrLoadingId = "";

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
    return view;
  }

  private void initializeList() {
    dataStore = new DataStore();
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
    ViewItemDecoration viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    competitionsListRecyclerAdapter = new CompetitionsListRecyclerAdapter(mContext);
    competitionsListRecyclerAdapter.setDeleteListener(this);
    competitionsListRecyclerAdapter.setLoadMoreCallback(this);
    competitionList.setLayoutManager(new LinearLayoutManager(mContext));
    competitionList.addItemDecoration(viewItemDecoration);
    competitionList.setAdapter(competitionsListRecyclerAdapter);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        refreshCompetitions();
      }
    });
  }

  private void refreshCompetitions() {
    dataStore.requestCompetitionLists(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    fetchCompetitions();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void fetchCompetitions() {
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
  public void onCompetitionsListAvailable(CompetitionListResponse competitionsResponse, boolean isAppendable) {
    List<CompetitionModel> myContests = filterMyCompetitions(competitionsResponse.getCompetitionModels());
    try {
      lastCompetitionId = competitionsResponse.getLastId();
      if (isAppendable) {
        if (myContests != null) {
          if (myContests.size() > 0) {
            competitionsListRecyclerAdapter.appendCompetitions((ArrayList<CompetitionModel>) myContests);
          } else {
            competitionsListRecyclerAdapter.noMoreCompetitionsAvailableToLoad();
          }
        } else {
          competitionsListRecyclerAdapter.noMoreCompetitionsAvailableToLoad();
        }
      } else {
        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
        setProgressVisibility(false);
        if (myContests != null) {
          if (myContests.size() == 0) {
            setMessagePanel(true, "No competitions!");
          } else {
            setMessagePanel(false, "");
            competitionsListRecyclerAdapter.setCompetitions(myContests);
          }
        } else {
          setMessagePanel(true, "Something went wrong!");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
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

  private List<CompetitionModel> filterMyCompetitions(List<CompetitionModel> competitions) {
    String myUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    List<CompetitionModel> myCompetitions = new ArrayList<>();
    for (int i = 0; i < competitions.size(); i++) {
      if (competitions.get(i).getmAdmin().getmUsername().equals(myUsername)) {
        myCompetitions.add(competitions.get(i));
      }
    }
    return myCompetitions;
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
    fetchCompetitions();
  }

  @Override
  public void loadMoreCompetitions() {
    fetchMoreCompetitions();
  }

  private void fetchMoreCompetitions() {
    if (lastLoadedOrLoadingId != lastCompetitionId) {
      dataStore.requestCompetitionLists(lastCompetitionId, this);
      lastLoadedOrLoadingId = lastCompetitionId;
    }
  }
}
