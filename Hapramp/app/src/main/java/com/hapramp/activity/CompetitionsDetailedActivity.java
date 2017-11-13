package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.CompetitionPostsRecyclerAdapter;
import com.hapramp.CompetitionsPostReponse;
import com.hapramp.PostsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CompetitionsPostFetchCallback;
import com.hapramp.models.response.CompetitionResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionsDetailedActivity extends AppCompatActivity implements CompetitionsPostFetchCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.feed_owner_pic)
    SimpleDraweeView feedOwnerPic;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.competitionDetails)
    TextView competitionDetails;
    @BindView(R.id.entry_fee_icon)
    TextView entryFeeIcon;
    @BindView(R.id.entry_fee_caption)
    TextView entryFeeCaption;
    @BindView(R.id.entry_fee)
    TextView entryFee;
    @BindView(R.id.entry_info_container)
    LinearLayout entryInfoContainer;
    @BindView(R.id.prize_money_icon)
    TextView prizeMoneyIcon;
    @BindView(R.id.prize_caption)
    TextView prizeCaption;
    @BindView(R.id.prize_money)
    TextView prizeMoney;
    @BindView(R.id.prize_info_container)
    LinearLayout prizeInfoContainer;
    @BindView(R.id.participantsCaption)
    TextView participantsCaption;
    @BindView(R.id.participantsRv)
    RecyclerView participantsRv;
    @BindView(R.id.submit_entries)
    TextView submitEntries;
    @BindView(R.id.competitionContentHolder)
    RelativeLayout competitionContentHolder;
    @BindView(R.id.submitted_post_caption)
    TextView submittedPostCaption;
    @BindView(R.id.submitted_posts_rv)
    RecyclerView submittedPostsRv;

    private CompetitionPostsRecyclerAdapter recyclerAdapter;
    private String compId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_competitions_detailed);
        ButterKnife.bind(this);
        init();
        attachListeners();
        fetchCompetitionsPosts();
    }

    private void init() {

        compId = getIntent().getExtras().getString("compId");
        recyclerAdapter = new CompetitionPostsRecyclerAdapter(this);
        submittedPostsRv.setLayoutManager(new LinearLayoutManager(this));
        submittedPostsRv.setAdapter(recyclerAdapter);

    }

    private void attachListeners() {

    }

    private void fetchCompetitionsPosts(){

        DataServer.getCompetitionsPosts(compId ,this);

    }

    @Override
    public void onCompetitionsPostsFetched(CompetitionsPostReponse competitionResponse) {
        recyclerAdapter.setCompetitionsPostReponses(competitionResponse.getPosts());
    }

    @Override
    public void onCompetionsPostFetchError() {
        Log.d("Competitions","Error fetching posts");
    }

}
