package com.hapramp.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.ResourceCreditCallback;
import com.hapramp.models.ResourceCreditModel;
import com.hapramp.views.CustomProgressBar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserCreditsFragment extends Fragment implements ResourceCreditCallback {
  @BindView(R.id.vp_label)
  TextView vpLabel;
  @BindView(R.id.voting_power_progress)
  CustomProgressBar votingPowerProgress;
  @BindView(R.id.rc_label)
  TextView rcLabel;
  @BindView(R.id.resource_credit_progress)
  CustomProgressBar resourceCreditProgress;
  @BindView(R.id.credit_available_label)
  TextView creditAvailableLabel;
  @BindView(R.id.progress_container)
  FrameLayout progressContainer;
  @BindView(R.id.comments_allowed)
  TextView commentsAllowed;
  @BindView(R.id.votes_allowed)
  TextView votesAllowed;
  @BindView(R.id.transfers_allowed)
  TextView transfersAllowed;
  @BindView(R.id.swipe_refresh)
  SwipeRefreshLayout swipeRefresh;
  @BindView(R.id.vote_value_label)
  TextView voteValueLabel;
  @BindView(R.id.vote_value)
  TextView voteValueTv;
  private String username;
  private Unbinder unbinder;
  private Context mContext;

  private DataStore dataStore;

  public UserCreditsFragment() {
    dataStore = new DataStore();
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public void onAttach(Context context) {
    this.mContext = context;
    super.onAttach(context);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_credits, container, false);
    unbinder = ButterKnife.bind(this, view);
    attachListeners();
    fetchResourceCredit();
    return view;
  }

  @Override
  public void onPause() {
    super.onPause();
    hideProgress();
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

  private void hideProgress() {
    if (progressContainer != null) {
      progressContainer.setVisibility(View.GONE);
    }
  }

  private void attachListeners() {
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        fetchResourceCredit();
      }
    });
  }

  private void fetchResourceCredit() {
    showProgress();
    dataStore.requestRc(username, this);
  }

  private void showProgress() {
    if (progressContainer != null) {
      progressContainer.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onResourceCreditAvailable(ResourceCreditModel resourceCreditModel) {
    hideProgress();
    try {
      if (swipeRefresh != null) {
        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
      }
      commentsAllowed.setText(String.valueOf(resourceCreditModel.getCommentAllowed() > 99 ?
        "99+" : resourceCreditModel.getCommentAllowed()));
      votesAllowed.setText(String.valueOf(resourceCreditModel.getVoteALlowed() > 99 ?
        "99+" : resourceCreditModel.getVoteALlowed()));
      transfersAllowed.setText(String.valueOf(resourceCreditModel.getTransferAllowed() > 99 ?
        "99+" : resourceCreditModel.getTransferAllowed()));
      resourceCreditProgress.setProgressPercent(resourceCreditModel.getResourceCreditPercentage());
      votingPowerProgress.setProgressPercent(resourceCreditModel.getVotingPercentage());
      formatAndSetVoteValue(resourceCreditModel.getVoteValue());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void formatAndSetVoteValue(double voteValue) {
    SpannableString vv = new SpannableString(String.format(Locale.US, "$ %.3f", voteValue));
    vv.setSpan(new RelativeSizeSpan(.5f), 0, 1, 0);
    vv.setSpan(new ForegroundColorSpan(Color.parseColor("#61000000")), 0, 1, 0);// set color
    voteValueTv.setText(vv);
  }

  @Override
  public void onResourceCreditError(String msg) {
    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
    hideProgress();
  }
}
