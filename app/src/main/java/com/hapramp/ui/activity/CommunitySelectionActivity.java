package com.hapramp.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.ui.callbacks.communityselection.CommunitySelectionPageCallback;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.viewmodel.communityselectionpage.CommunitySelectionPageViewModel;
import com.hapramp.views.CommunitySelectionView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 *  This activity is responsible for community selection by user.
 *  Activity is opened when User has not selected this earlier.
 *  LoginActivity gets all the relevant about user after successful login.
 *  After which decisions are taken.
 * */

public class CommunitySelectionActivity extends BaseActivity implements CommunitySelectionPageCallback, CommunitySelectionView.CommunitySelectionListener {
  public static final String TAG = CommunitySelectionActivity.class.getSimpleName();
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.communitySelectionView)
  CommunitySelectionView communitySelectionView;
  @BindView(R.id.toolbar_drop_shadow)
  FrameLayout toolbarDropShadow;
  @BindView(R.id.communityContinueButton)
  TextView communityContinueButton;
  @BindView(R.id.communityLoadingProgressBar)
  ProgressBar communityLoadingProgressBar;
  private CommunitySelectionPageViewModel communitySelectionPageViewModel;

  @Override
  protected void onStart() {
    super.onStart();
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_COMMUNITY, null);
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_COMMUNITY_SELECTION);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_community_selection);
    ButterKnife.bind(this);
    String cachedCommunity = HaprampPreferenceManager.getInstance().
      getUserSelectedCommunityAsJson();
    if (cachedCommunity.length() > 0) {
      CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance().
        getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      if (cwr != null) {
        if (cwr.getCommunityModels().size() > 0) {
          navigateToHome();
          return;
        }
      }
    }
    init();
  }

  private void navigateToHome() {
    Intent i = new Intent(this, HomeActivity.class);
    startActivity(i);
    finish();
  }

  private void init() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "community selection init");
    communitySelectionView.setCommunitySelectionListener(this);
    communitySelectionPageViewModel = ViewModelProviders.of(this).get(CommunitySelectionPageViewModel.class);
    communitySelectionPageViewModel.getCommunities(this)
      .observe(this, new Observer<List<CommunityModel>>() {
        @Override
        public void onChanged(@Nullable List<CommunityModel> communityModels) {
          communitySelectionView.setCommunityList(communityModels);
          if (communityLoadingProgressBar != null) {
            communityLoadingProgressBar.setVisibility(View.GONE);
          }
          HaprampPreferenceManager.getInstance().saveAllCommunityListAsJson(new Gson().toJson(new CommunityListWrapper(communityModels)));
        }
      });
    communityContinueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showProgressDialog("Updating your communities choice....", true);
        communitySelectionPageViewModel.updateServer(communitySelectionView.getSelectionList());
      }
    });
  }

  @Override
  public void onCommunityFetchFailed() {
    toast(getString(R.string.failed_to_fetch_communities));
  }

  @Override
  public void onCommunityUpdated(List<CommunityModel> selectedCommunities) {
    showProgressDialog("", false);
    toast(getString(R.string.community_updated));
    HaprampPreferenceManager.getInstance().saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(selectedCommunities)));
    navigateToHome();
  }

  @Override
  public void onCommunityUpdateFailed() {
    showProgressDialog("", false);
    toast(getString(R.string.failed_to_update_communities));
  }

  @Override
  public void onCommunitySelectionChanged(List<Integer> selections) {
    communityContinueButton.setEnabled(selections.size() > 0);
  }
}
