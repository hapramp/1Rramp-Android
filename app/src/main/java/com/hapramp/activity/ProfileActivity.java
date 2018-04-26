package com.hapramp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.ProfileRecyclerAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity {


    private static final int POST_LIMIT = 100;
    @BindView(R.id.closeBtn) TextView closeBtn;
    @BindView(R.id.toolbar_container) RelativeLayout toolbarContainer;
    @BindView(R.id.profilePostRv) RecyclerView profilePostRv;
    @BindView(R.id.profile_user_name) TextView profileUserName;

    private String username;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        init();
        attachListeners();
        fetchProfilePosts();

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

// TODO: 4/7/2018 implement lazy loading later on
//        try {
//            if (currentPostResponse.next.length() == 0) {
//                return;
//            }
//
//            fetchProfilePosts();
//
//        } catch (Exception e) {
//
//        }
    }

    private void init() {

        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        if (getIntent() == null) {
            Toast.makeText(this, "No Username Passed", Toast.LENGTH_SHORT).show();
            return;
        }

        username = getIntent().getExtras().getString(Constants.EXTRAA_KEY_STEEM_USER_NAME, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
        //todo: make sure `username` of steem account
        profilePostAdapter = new ProfileRecyclerAdapter(this, username);

        llm = new LinearLayoutManager(this);
        profilePostRv.setLayoutManager(llm);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(false,0);
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

    private void fetchProfilePosts() {

        Log.d("ProfilePost", Profile.getDefaultProfileAsJson());

        RetrofitServiceGenerator.getService()
                .getPostsOfUser(username, POST_LIMIT)
                .enqueue(new Callback<List<Feed>>() {
                    @Override
                    public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                        if (response.isSuccessful()) {
                            bindProfilePosts(response.body());
                        } else {
                            failedToFetchUserPosts();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Feed>> call, Throwable t) {
                        failedToFetchUserPosts();
                    }
                });

    }

    private void bindProfilePosts(List<Feed> body) {
        //Profile.fetchUserProfilesFor(body);
        profilePostAdapter.setPosts(body);
    }

    private void failedToFetchUserPosts() {

    }


}
