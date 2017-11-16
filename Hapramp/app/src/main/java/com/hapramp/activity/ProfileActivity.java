package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.R;
import com.hapramp.adapters.ProfilePostAdapter;
import com.hapramp.adapters.ProfileSkillsRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FollowUserCallback;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.requests.FollowRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.utils.FontManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity implements FullUserDetailsCallback, PostFetchCallback, ProfileSkillsRecyclerAdapter.OnCategoryItemClickListener, FollowUserCallback {

    @BindView(R.id.profile_progress_bar)
    ProgressBar profileProgressBar;
    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
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
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.trophies_count)
    TextView trophiesCount;
    @BindView(R.id.badge_containers)
    LinearLayout badgeContainers;
    @BindView(R.id.recentPostsCaption)
    TextView recentPostsCaption;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;
    @BindView(R.id.categoryLoadingProgress)
    ProgressBar categoryLoadingProgress;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.profile_content)
    ScrollView profileContent;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    private String userId;
    private ProfilePostAdapter profilePostAdapter;
    private ProfileSkillsRecyclerAdapter profileSkillsRecyclerAdapter;
    private boolean followed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        init();
        attachListeners();
        requestData();
    }

    private void requestData() {

        profileContent.setVisibility(View.GONE);
        fetchProfileData();
        fetchProfilePosts(0); // 0 - for loading all posts

    }

    private void init() {

        userId = getIntent().getExtras().getString("userId");

        profilePostAdapter = new ProfilePostAdapter(this);
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        overflowBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        profileSkillsRecyclerAdapter = new ProfileSkillsRecyclerAdapter(this, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(profileSkillsRecyclerAdapter);
        profilePostRv.setLayoutManager(new LinearLayoutManager(this));
        profilePostRv.setAdapter(profilePostAdapter);
        profilePostRv.setNestedScrollingEnabled(false);

    }

    private void attachListeners() {
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setUserFollow(!followed);
            }
        });
    }

    private void fetchProfileData() {

        DataServer.getFullUserDetails(userId, this);

    }

    private void fetchProfilePosts(int skill_id) {
        DataServer.getPosts(skill_id, this);
    }

    @Override
    public void onFullUserDetailsFetched(UserModel userModel) {

        profileContent.setVisibility(View.VISIBLE);
        // TODO: 11/16/2017 set user followed
        try {
            profilePic.setImageURI(userModel.getImage_uri());
            username.setText(userModel.getUsername());
            hapname.setText("@hapname");
            bio.setText("-----Bio----- Here-----");
            String _t = String.format(getResources().getString(R.string.profile_posts_count_caption), userModel.getSkills().size());
            postCounts.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_followers_caption), userModel.followers);
            followersCount.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_following_count_caption), userModel.followings);
            followingsCount.setText(_t);
            hapcoinsCount.setText(String.valueOf(userModel.hapcoins));
            trophiesCount.setText("0");
            bindSkillsCategory(userModel.getSkills());

        } catch (Exception e) {

        }

    }

    private void bindPosts(List<PostResponse> posts) {

        hideContentLoadingProgress();
        profilePostAdapter.setPostResponses(posts);

    }

    private void bindSkillsCategory(List<UserModel.Skills> skills) {

        hideCategoryLoadingProgress();
        skills.add(0, new UserModel.Skills(0, "All", "", ""));
        profileSkillsRecyclerAdapter.setCategories(skills);

    }

    private void setUserFollow(boolean state){
        DataServer.setFollowUser(
                userId,
                new FollowRequestBody(state),this
        );
    }

    private void showContentLoadingProgress() {
        if (contentLoadingProgress != null)
            contentLoadingProgress.setVisibility(View.VISIBLE);
    }

    private void hideContentLoadingProgress() {
        if (contentLoadingProgress != null)
            contentLoadingProgress.setVisibility(View.GONE);
    }

    private void showCategoryLoadingProgress() {
        if (categoryLoadingProgress != null)
            categoryLoadingProgress.setVisibility(View.VISIBLE);
    }

    private void hideCategoryLoadingProgress() {
        if (categoryLoadingProgress != null)
            categoryLoadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onCategoryClicked(int id) {

        // fetch the selected posts
        fetchProfilePosts(id);

    }

    @Override
    public void onPostFetched(List<PostResponse> postResponses) {
        if (postResponses.size() > 0) {
            emptyMessage.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.VISIBLE);
        }
        bindPosts(postResponses);
    }

    @Override
    public void onPostFetchError() {
        Toast.makeText(this, "Error Fetching Your Posts...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserFollowSet(boolean state) {
        setFollowState(state);
    }

    @Override
    public void onUserFollowSetFailed(boolean state) {
        setFollowState(state);
    }

    private void setFollowState(boolean state){
        if(state){
            followBtn.setText("Following");
            followed = true;
        }else{
            followBtn.setText("Follow");
            followed = false;
        }
    }

}
