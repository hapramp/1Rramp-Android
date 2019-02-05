package com.hapramp.ui.fragments;


import android.app.ProgressDialog;
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
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.draft.DraftListItemModel;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.models.DraftUploadResponse;
import com.hapramp.ui.adapters.DraftsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDraftsFragment extends Fragment implements DraftsHelper.DraftsHelperCallback, DraftsAdapter.EmptyDraftsAdapterCallback {
  private static final String NO_DRAFT_MESSAGE = "You don’t have any saved drafts.";
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
  private ProgressDialog progressDialog;

  {
    setMessagePanel(true, "Failed to load Drafts!");
  }

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
    return view;
  }

  private void initialize() {
    draftsAdapter = new DraftsAdapter(context);
    draftsAdapter.setEmptyDraftsAdapter(this);
    draftsHelper = new DraftsHelper();
    draftsHelper.setDraftsHelperCallback(this);
    draftsList.setLayoutManager(new LinearLayoutManager(context));
    draftsList.setAdapter(draftsAdapter);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        refreshDrafts();
      }
    });
  }

  private void refreshDrafts() {
    loadDrafts();
  }

  private void loadDrafts() {
    //fetch drafts from server
    RetrofitServiceGenerator.getService().getAllDrafts().enqueue(new Callback<List<DraftUploadResponse>>() {
      @Override
      public void onResponse(Call<List<DraftUploadResponse>> call, Response<List<DraftUploadResponse>> response) {
        if (response.isSuccessful()) {
          onAllDraftsLoaded(response.body());
        }
      }

      @Override
      public void onFailure(Call<List<DraftUploadResponse>> call, Throwable t) {

      }
    });
  }

  private void onAllDraftsLoaded(List<DraftUploadResponse> drafts) {
    if (drafts != null) {
      List<DraftListItemModel> draftListItemModels = new ArrayList<>();
      int size = drafts.size();
      //invalidate UI
      for (int i = 0; i < size; i++) {
        DraftUploadResponse draftItem = drafts.get(i);
        if (draftItem.getmDraftType() != null) {
          //list object
          DraftListItemModel draftListItemModel = new DraftListItemModel();
          //set data
          draftListItemModel.setTitle(draftItem.getmTitle());
          draftListItemModel.setDraftType(draftItem.getmDraftType());
          draftListItemModel.setDraftId(draftItem.getmId());
          draftListItemModel.setJson(draftItem.getmBody());
          draftListItemModel.setLastModified(draftItem.getmLastModifiedAt());
          draftListItemModels.add(draftListItemModel);
        }
      }
      draftsLoaded(draftListItemModels.size());
      draftsAdapter.setDrafts(draftListItemModels);
    } else {
      draftsLoaded(0);
    }
  }

  private void draftsLoaded(int size) {
    setProgressVisibility(false);
    if (swipeRefresh != null) {
      if (swipeRefresh.isRefreshing()) {
        swipeRefresh.setRefreshing(false);
      }
    }
    if (size > 0) {
      setMessagePanel(false, "");
    } else {
      setMessagePanel(true, NO_DRAFT_MESSAGE);
    }
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
  public void onResume() {
    super.onResume();
    loadDrafts();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onNewDraftSaved(boolean success,int draftId) {

  }

  @Override
  public void onDraftUpdated(boolean success,int draftId) {

  }

  @Override
  public void onDraftDeleted(boolean success) {

  }

  private void showDialog(boolean show, String message) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(context);
    } else {
      if (show) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    }
  }

  @Override
  public void onAllDraftsDeleted() {
    setMessagePanel(true, NO_DRAFT_MESSAGE);
  }
}
