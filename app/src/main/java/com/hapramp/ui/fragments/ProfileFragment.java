package com.hapramp.ui.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
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
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements ServiceWorkerCallback {

    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    private Context mContext;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private Unbinder unbinder;
    private LinearLayoutManager llm;
    private String username;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ServiceWorker serviceWorker;
    private ServiceWorkerRequestBuilder serviceWorkerRequestParamsBuilder;
    private ServiceWorkerRequestParams serviceWorkerRequestParams;
    private String lastAuthor;
    private String lastPermlink;

    public ProfileFragment() {
        Crashlytics.setString(CrashReporterKeys.UI_ACTION,"profile fragment");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        fetchPosts();
        return view;
    }

    private void init() {
        profilePostAdapter = new ProfileRecyclerAdapter(mContext, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(false, 0);
        profilePostRv.addItemDecoration(viewItemDecoration);
        llm = new LinearLayoutManager(mContext);
        profilePostRv.setLayoutManager(llm);
        profilePostRv.setAdapter(profilePostAdapter);
        setScrollListener();
        prepareServiceWorker();
        fetchPosts();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void prepareServiceWorker() {
        serviceWorker = new ServiceWorker();
        serviceWorker.init(getActivity());
        serviceWorker.setServiceWorkerCallback(this);
    }

    private void fetchPosts() {
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder
                .setUserName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
                .setLimit(Constants.MAX_FEED_LOAD_LIMIT)
                .createRequestParam();
        serviceWorker.requestProfilePosts(serviceWorkerRequestParams);
    }

    private void loadMore() {
        serviceWorkerRequestParamsBuilder = new ServiceWorkerRequestBuilder();
        serviceWorkerRequestParams = serviceWorkerRequestParamsBuilder
                .setLastAuthor(lastAuthor)
                .setLastPermlink(lastPermlink)
                .createRequestParam();
        serviceWorker.requestAppendableProfilePosts(serviceWorkerRequestParams);
    }

    public void reloadPosts() {
        fetchPosts();
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
        if (profilePostAdapter != null) {
            profilePostAdapter.setPosts(cachedList);
        }
    }

    @Override
    public void onFetchingFromServer() {

    }

    @Override
    public void onFeedsFetched(ArrayList<Feed> fetchedFeeds, String lastAuthor, String lastPermlink) {
        if (profilePostAdapter != null) {
            profilePostAdapter.setPosts(fetchedFeeds);
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
        if (profilePostAdapter != null) {
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
        if (profilePostAdapter != null) {
            profilePostAdapter.appendPost(appendableList);
            this.lastAuthor = lastAuthor;
            this.lastPermlink = lastPermlink;
        }
    }

    @Override
    public void onAppendableDataLoadingFailed() {

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
}
