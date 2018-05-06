package com.hapramp.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.ui.activity.HowToEarnActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.UserStatsCallback;
import com.hapramp.datamodels.response.UserStatsModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EarningFragment extends Fragment implements UserStatsCallback {


    @BindView(R.id.post_created_count)
    TextView postCreatedCount;
    @BindView(R.id.post_created_text)
    TextView postCreatedText;
    @BindView(R.id.post_rated_count)
    TextView postRatedCount;
    @BindView(R.id.post_rated_text)
    TextView postRatedText;
    @BindView(R.id.topWrapper)
    LinearLayout topWrapper;
    @BindView(R.id.earned_badge)
    TextView earnedBadge;
    @BindView(R.id.how_to_redeem)
    TextView howToRedeem;
    @BindView(R.id.bottomWrapper)
    RelativeLayout bottomWrapper;
    Unbinder unbinder;
    @BindView(R.id.post_created_icon)
    TextView postCreatedIcon;
    @BindView(R.id.post_rated_icon)
    TextView postRatedIcon;

    private static String userId;
    public EarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        postCreatedIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        postRatedIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        howToRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHowToRedeemPage();
            }
        });
        userId = HaprampPreferenceManager.getInstance().getUserId();
        fetchUserStats();
    }

    private void showHowToRedeemPage() {
        Intent intent = new Intent(getActivity(), HowToEarnActivity.class);
        startActivity(intent);
    }

    private void fetchUserStats(){
        DataServer.getUserStats(userId,this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onUserStatsFetched(UserStatsModel stats) {
        bindStats(stats);
    }

    private void bindStats(UserStatsModel stats) {
        try {
            postCreatedCount.setText(String.valueOf(stats.posts));
            postRatedCount.setText(String.valueOf(stats.rated));
            postCreatedText.setText(String.format(getResources().getString(R.string.post_created),stats.posts));
            earnedBadge.setText(String.format(getResources().getString(R.string.earnedHapcoins), stats.hapcoins));
            postRatedText.setText(String.format(getResources().getString(R.string.post_rated),stats.rated));

        }catch (NullPointerException e){
            // not handled
        }

    }

    @Override
    public void onUserStatsFetchError() {

    }
}
