package com.hapramp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.draft.ContestDraftModel;
import com.hapramp.draft.DraftListItemModel;
import com.hapramp.draft.DraftType;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.ui.adapters.DraftsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xute.markdeditor.models.DraftModel;

public class MyDraftsFragment extends Fragment implements DraftsHelper.BlogDraftsDatabaseCallbacks, DraftsHelper.ContestDraftsDatabaseCallbacks {
  @BindView(R.id.drafts_list)
  RecyclerView draftsList;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  Unbinder unbinder;
  private Context context;
  private DraftsHelper draftsHelper;
  private DraftsAdapter draftsAdapter;

  public MyDraftsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_my_drafts, container, false);
    unbinder = ButterKnife.bind(this, view);
    initialize();
    readDrafts();
    return view;
  }

  private void initialize() {
    draftsAdapter = new DraftsAdapter(context);
    draftsHelper = new DraftsHelper(context);
    draftsHelper.setBlogDraftCallbacks(this);
    draftsHelper.setContestDraftCallbacks(this);
    draftsList.setLayoutManager(new LinearLayoutManager(context));
    draftsList.setAdapter(draftsAdapter);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        setProgressVisibility(true);
        readDrafts();
      }
    });
  }

  private void readDrafts() {
    draftsHelper.fetchAllTypeOfDrafts();
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
  public void onDraftInserted(boolean success) {

  }

  @Override
  public void onSingleBlogDraftRead(DraftModel draft) {

  }

  @Override
  public void onAllDraftsRead(ArrayList<DraftListItemModel> drafts) {
    setProgressVisibility(false);
    if (swipeRefresh != null) {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
    }
    if (drafts != null) {
      if (drafts.size() > 0) {
        setMessagePanel(false, "");
        draftsAdapter.setDrafts(drafts);
      } else {
        setMessagePanel(true, "No Drafts!");
      }
    } else {
      setMessagePanel(true, "Failed to load Drafts!");
    }
  }

  @Override
  public void onDraftUpdated(boolean success) {

  }

  @Override
  public void onDraftDeleted(boolean success) {

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
  public void onSingleContestDraftRead(ContestDraftModel draft) {

  }
}
