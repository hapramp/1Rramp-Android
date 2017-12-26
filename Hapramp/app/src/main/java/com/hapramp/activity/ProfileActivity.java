package com.hapramp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.adapters.ProfileSkillsRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FollowUserCallback;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.requests.FollowRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.InterestsView;
import com.hapramp.views.SelectableInterestsView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity implements FullUserDetailsCallback, PostFetchCallback, FollowUserCallback {


    @BindView(R.id.profile_progress_bar)
    ProgressBar profileProgressBar;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
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
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.profile_content)
    ScrollView profileContent;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    private String userId;

    private PostsRecyclerAdapter profilePostAdapter;
    private ProfileSkillsRecyclerAdapter profileSkillsRecyclerAdapter;
    private boolean followed = false;
    private String TICK_TEXT = "\u2713";
    private ViewItemDecoration viewItemDecoration;

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
        profilePostAdapter = new PostsRecyclerAdapter(this, profilePostRv);
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        overflowBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        profilePostRv.setLayoutManager(new LinearLayoutManager(this));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        profilePostRv.addItemDecoration(viewItemDecoration);
        profilePostRv.setAdapter(profilePostAdapter);
        profilePostRv.setNestedScrollingEnabled(false);

    }

    private void attachListeners() {

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        if (skill_id == -1) {
            // get all post of this user
            DataServer.getPostsByUserId(URLS.POST_FETCH_START_URL, Integer.valueOf(userId), this);
        } else {
            // get post by user and skills
            DataServer.getPosts(URLS.POST_FETCH_START_URL, skill_id, Integer.valueOf(userId), this);

        }

    }

    @Override
    public void onFullUserDetailsFetched(UserModel userModel) {

        profileContent.setVisibility(View.VISIBLE);
        // TODO: 11/16/2017 set user followed
        try {
            //profilePic.setImageURI(userModel.getImage_uri());
            ImageHandler.loadCircularImage(this, profilePic, userModel.image_uri);
            username.setText(userModel.username);
            hapname.setText("@hapname");
            bio.setText(userModel.bio);
            String _t = String.format(getResources().getString(R.string.profile_posts_count_caption), userModel.skills.size());
            postCounts.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_followers_caption), userModel.followers);
            followersCount.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_following_count_caption), userModel.followings);
            followingsCount.setText(_t);
            interestsView.setInterests(userModel.skills);

        } catch (Exception e) {

        }

    }

    private void setUserFollow(boolean state) {
        DataServer.setFollowUser(
                userId,
                new FollowRequestBody(state), this
        );
    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onPostFetched(PostResponse postResponses) {

        if (postResponses.results.size() > 0) {
            emptyMessage.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.VISIBLE);
        }

        bindPosts(postResponses.results);

    }

    private void bindPosts(List<PostResponse.Results> results) {
        profilePostAdapter.appendResult(results);
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

    private void setFollowState(boolean state) {

        if (state) {
            followBtn.setText(TICK_TEXT + " Following");
            followed = true;
        } else {
            followBtn.setText("Follow");
            followed = false;
        }

    }

}
