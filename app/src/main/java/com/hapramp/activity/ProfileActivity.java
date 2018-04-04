package com.hapramp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.ProfileRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity implements FullUserDetailsCallback, PostFetchCallback {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    @BindView(R.id.profile_user_name)
    TextView profileUserName;
    private String userId;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private PostResponse currentPostResponse;
    private LinearLayoutManager llm;

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
        fetchProfileData();
    }


    public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

        // use your LayoutManager instead
        private LinearLayoutManager lm;

        EndlessOnScrollListener(LinearLayoutManager llm) {
            this.lm = llm;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }
        }

        public abstract void onScrolledToEnd();

    }

    private void setScrollListener() {

        profilePostRv.addOnScrollListener(new EndlessOnScrollListener(llm) {
            @Override
            public void onScrolledToEnd() {
                loadMore();
            }
        });
    }

    private void loadMore() {

        try {
            if (currentPostResponse.next.length() == 0) {
                return;
            }

            fetchProfilePosts(currentPostResponse.next);

        } catch (Exception e) {

        }
    }

    private void init() {

        userId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_USER_ID);
        //todo: make sure userId is `username` of steem account
        profilePostAdapter = new ProfileRecyclerAdapter(this,userId);
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        overflowBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        llm = new LinearLayoutManager(this);
        profilePostRv.setLayoutManager(llm);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(false);
        profilePostRv.addItemDecoration(viewItemDecoration);
        profilePostRv.setAdapter(profilePostAdapter);
        setScrollListener();

    }

    private void attachListeners() {

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fetchProfileData() {

        DataServer.getFullUserDetails(userId, this);

    }

    private void fetchProfilePosts(String url) {

        // get all post of this user
        DataServer.getPostsByUserId(url, Integer.valueOf(userId), this);

    }

    @Override
    public void onFullUserDetailsFetched(UserModel userModel) {

        ProfileHeaderModel profileHeaderModel = new ProfileHeaderModel(
                userModel.id,
                userModel.image_uri,
                userModel.username,
                "@hapname",
                userModel.id == Integer.valueOf(HaprampPreferenceManager.getInstance().getUserId()), // if its mine then its editable
                userModel.bio,
                0,
                userModel.followers,
                userModel.followings,
                userModel.skills);

        profileUserName.setText(userModel.username);
      //  profilePostAdapter.setProfileHeaderModel(profileHeaderModel);
        // start loading with given default limits
        fetchProfilePosts(URLS.POST_FETCH_START_URL);

        showContent(true);

    }

    private void showContent(boolean show) {
        if (show) {
            //hide progress bar
            if (contentLoadingProgress != null) {
                contentLoadingProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onPostFetched(PostResponse postResponses) {

        currentPostResponse = postResponses;
       // profilePostAdapter.setHasMoreToLoad(currentPostResponse.next.length() > 0);
        bindPosts(postResponses.results);

    }

    private void bindPosts(List<Feed> results) {
     //   profilePostAdapter.appendResult(results);
    }

    @Override
    public void onPostFetchError() {
        Toast.makeText(this, "Error Fetching Your Posts...", Toast.LENGTH_SHORT).show();
    }


}
