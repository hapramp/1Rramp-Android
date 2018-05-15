package com.hapramp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datastore.ServiceWorker;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ServiceWorkerRequestBuilder;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.ProfileRecyclerAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.FeedResponse;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity implements ServiceWorkerCallback {
    private static final int POST_LIMIT = 100;
    @BindView(R.id.closeBtn) TextView closeBtn;
    @BindView(R.id.toolbar_container) RelativeLayout toolbarContainer;
    @BindView(R.id.profilePostRv) RecyclerView profilePostRv;
    @BindView(R.id.profile_user_name) TextView profileUserName;
    private String username;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private LinearLayoutManager llm;
    private ServiceWorker serviceWorker;
    private ServiceWorkerRequestBuilder serviceWorkerRequestParamsBuilder;
    private ServiceWorkerRequestParams serviceWorkerRequestParams;
    private String lastAuthor;
    private String lastPermlink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        init();
        attachListeners();
        prepareServiceWorker();
        fetchPosts();
        AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_PROFILE,null);
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

    private void init() {
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        if (getIntent() == null) {
            Toast.makeText(this, "No Username Passed", Toast.LENGTH_SHORT).show();
            return;
        }
        username = getIntent().getExtras().getString(Constants.EXTRAA_KEY_STEEM_USER_NAME, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
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


    private void prepareServiceWorker() {
        serviceWorker = new ServiceWorker();
        serviceWorker.init(this);
        serviceWorker.setServiceWorkerCallback(this);
    }

    private void fetchPosts() {
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder
                .setUserName(username)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .createRequestParam();
        serviceWorker.requestProfilePosts(serviceWorkerRequestParams);
    }

    private void loadMore() {
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder
                .setUserName(username)
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .setLastAuthor(lastAuthor)
                .setLastPermlink(lastPermlink)
                .createRequestParam();
        serviceWorker.requestAppendableProfilePosts(serviceWorkerRequestParams);
    }

    @Override
    public void onLoadingFromCache() {

    }

    @Override
    public void onCacheLoadFailed() {

    }

    @Override
    public void onNoDataInCache() {

    }

    @Override
    public void onLoadedFromCache(ArrayList<Feed> cachedList, String lastAuthor, String lastPermlink) {
        if(profilePostAdapter!=null){
            profilePostAdapter.setPosts(cachedList);
        }
    }

    @Override
    public void onFetchingFromServer() {

    }

    @Override
    public void onFeedsFetched(ArrayList<Feed> fetched, String lastAuthor, String lastPermlink) {
        if(profilePostAdapter!=null){
            Log.d("ProfileActivity","size"+fetched.size());
            profilePostAdapter.setPosts(fetched);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }
    }

    @Override
    public void onFetchingFromServerFailed() {

    }

    @Override
    public void onNoDataAvailable() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onRefreshed(List<Feed> refreshedList, String lastAuthor, String lastPermlink) {
        if(profilePostAdapter!=null){
            profilePostAdapter.setPosts(refreshedList);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }
    }

    @Override
    public void onRefreshFailed() {

    }

    @Override
    public void onLoadingAppendableData() {

    }

    @Override
    public void onAppendableDataLoaded(List<Feed> appendableList, String lastAuthor, String lastPermlink) {
        if(profilePostAdapter!=null){
            profilePostAdapter.appendPost(appendableList);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }
    }

    @Override
    public void onAppendableDataLoadingFailed() {

    }
}
