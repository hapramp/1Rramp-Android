package com.hapramp.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.ui.callbacks.communityselection.CommunitySelectionPageCallback;
import com.hapramp.viewmodel.CommunitySelectionPageViewModel;
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

public class CommunitySelectionActivity extends BaseActivity implements CommunitySelectionPageCallback {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.communitySelectionView)
    CommunitySelectionView communitySelectionView;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.communityContinueButton)
    TextView communityContinueButton;

    public static final String TAG = CommunitySelectionActivity.class.getSimpleName();
    private CommunitySelectionPageViewModel communitySelectionPageViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_selection);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        communitySelectionPageViewModel = ViewModelProviders.of(this).get(CommunitySelectionPageViewModel.class);
        communitySelectionPageViewModel.getCommunities(this).observe(this, new Observer<List<CommunityModel>>() {
            @Override
            public void onChanged(@Nullable List<CommunityModel> communityModels) {
                communitySelectionView.setCommunityList(communityModels);
                HaprampPreferenceManager.getInstance().saveAllCommunityListAsJson(new Gson().toJson(new CommunityListWrapper(communityModels)));
            }
        });
        communityContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communitySelectionPageViewModel.updateServer(communitySelectionView.getSelectionList());
            }
        });
    }

    private void navigateToHome() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onCommunityFetchFailed() {
        toast(getString(R.string.failed_to_fetch_communities));
    }

    @Override
    public void onCommunityUpdated(List<CommunityModel> selectedCommunities) {
        toast(getString(R.string.community_updated));
        HaprampPreferenceManager.getInstance().saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(selectedCommunities)));
        navigateToHome();
    }

    @Override
    public void onCommunityUpdateFailed() {
        toast(getString(R.string.failed_to_update_communities));
    }
}
