package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datastore.WebScrapper;
import com.hapramp.datastore.callbacks.ResourceCreditCallback;
import com.hapramp.models.ResourceCreditModel;
import com.hapramp.views.CustomProgressBar;

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
  private String username;
  private Unbinder unbinder;
  private WebScrapper webScrapper;

  public UserCreditsFragment() {
    webScrapper = new WebScrapper();
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public void onAttach(Context context) {
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

  private void fetchResourceCredit() {
    showProgress();
    webScrapper.scrapResourceCreditInfo(username, this);
  }

  private void showProgress() {
    if (progressContainer != null) {
      progressContainer.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onResourceCreditAvailable(ResourceCreditModel resourceCreditModel) {
    try {
      votingPowerProgress.setProgressPercent(resourceCreditModel.getVotingPowerPercentage());
      resourceCreditProgress.setProgressPercent(resourceCreditModel.getResourceCreditPercentage());
      commentsAllowed.setText(resourceCreditModel.getCommentsAllowed());
      votesAllowed.setText(resourceCreditModel.getVotesAllowed());
      transfersAllowed.setText(resourceCreditModel.getTransfersAllowed());
    }
    catch (Exception e) {
      Log.d("UserCredit",e.toString());
    }
    hideProgress();
  }

  @Override
  public void onResourceCreditError(String msg) {
    Log.d("UserCredit",msg);
    hideProgress();
  }
}
