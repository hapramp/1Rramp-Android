package com.hapramp.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.activity.ProfileEditActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FollowUserCallback;
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.requests.FollowRequestBody;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/30/2017.
 */

public class ProfileHeaderView extends FrameLayout implements FollowUserCallback {

    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @BindView(R.id.edit_btn)
    TextView editBtn;
    @BindView(R.id.follow_btn)
    TextView followBtn;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.divider_top)
    FrameLayout dividerTop;
    @BindView(R.id.post_counts)
    TextView postCounts;
    @BindView(R.id.followers_count)
    TextView followersCount;
    @BindView(R.id.followings_count)
    TextView followingsCount;
    @BindView(R.id.post_stats)
    LinearLayout postStats;
    @BindView(R.id.divider_bottom)
    FrameLayout dividerBottom;
    @BindView(R.id.interestCaption)
    TextView interestCaption;
    @BindView(R.id.interestsView)
    InterestsView interestsView;
    @BindView(R.id.postsCaption)
    TextView postsCaption;

    private Context mContext;
    private String TICK_TEXT = "\u2713";
    private ProfileHeaderModel profileData;
    private boolean isFollowed = false;

    public ProfileHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.profile_header_view, this);
        ButterKnife.bind(this,view);
    }

    public void setProfileHeaderData(ProfileHeaderModel profileData){
        this.profileData = profileData;
        bind(profileData);
    }

    private void bind(ProfileHeaderModel profileHeaderModel) {

        ImageHandler.loadCircularImage(mContext, profilePic, profileHeaderModel.getDpUrl());
        username.setText(profileHeaderModel.getUserName());
        hapname.setText("@hapname");
        bio.setText(profileHeaderModel.getBio() != null ? profileHeaderModel.getBio() : "");
        followersCount.setText(String.format(mContext.getResources().getString(R.string.profile_followers_caption), profileHeaderModel.getFollowers()));
        followingsCount.setText(String.format(mContext.getResources().getString(R.string.profile_following_count_caption), profileHeaderModel.getFollowing()));
        interestsView.setInterests(profileHeaderModel.getInterests());

        if(profileHeaderModel.isEditable()){

            followBtn.setVisibility(GONE);
            editBtn.setVisibility(View.VISIBLE);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToProfileEditActivity();
                }
            });

        }else{

            editBtn.setVisibility(GONE);
            followBtn.setVisibility(View.VISIBLE);
            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setUserFollow(!isFollowed);
                }
            });
        }

    }

    private void navigateToProfileEditActivity() {

        Intent intent = new Intent(mContext, ProfileEditActivity.class);
        mContext.startActivity(intent);

    }

    private void setUserFollow(boolean follow) {
        DataServer.setFollowUser(
                String.valueOf(profileData.getUserId()),
                new FollowRequestBody(follow),this);
    }

    @Override
    public void onUserFollowSet(boolean state) {
        setFollowState(state);
    }

    @Override
    public void onUserFollowSetFailed(boolean state) {
        setFollowState(state);
    }

    private void setFollowState(boolean state) {

        if (state) {
            followBtn.setText(TICK_TEXT + " Following");
            isFollowed = true;
        } else {
            followBtn.setText("Follow");
            isFollowed = false;
        }

    }
}
