package com.hapramp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.adapters.CategoryRecyclerAdapter;
import com.hapramp.api.URLS;
import com.hapramp.datastore.ServiceWorker;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.logger.L;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.ServiceWorkerRequestBuilder;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.feedlist.FeedListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements
        CategoryRecyclerAdapter.OnCategoryItemClickListener, LikePostCallback, FeedListView.FeedListViewListener, ServiceWorkerCallback {

    @BindView(R.id.feedListView)
    FeedListView feedListView;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;

    private Context mContext;
    private PostResponse currentPostReponse;
    private int currentSelectedSkillId;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    ServiceWorker serviceWorker;
    private Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceWorker = new ServiceWorker();
        serviceWorker.init(getActivity());
        serviceWorker.setServiceWorkerCallback(this);
        ServiceWorkerRequestParams serviceWorkerRequestParams =
                new ServiceWorkerRequestBuilder()
                        .serCommunityId("0")
                        .setUserName("unittestaccount")
                        .setLimit(10)
                        .createRequestParam();

        serviceWorker.requestFeeds(serviceWorkerRequestParams);

    }

    @Override
    public void onPause() {
        super.onPause();
        currentPostReponse = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initCategoryView();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedListView.setFeedListViewListener(this);
        feedListView.initialLoading();
        fetchPosts(0);

    }


    private void bringBackCategorySection() {
        sectionsRv.animate().translationY(0);
    }

    private void hideCategorySection() {
        sectionsRv.animate().translationY(-sectionsRv.getMeasuredHeight());
    }

    private void initCategoryView() {

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(categoryRecyclerAdapter);

        CommunityListWrapper cwr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
        ArrayList<CommunityModel> communityModels = new ArrayList<>();
        communityModels.add(0, new CommunityModel("", "", "", "", "All", 0));
        communityModels.addAll(cwr.getCommunityModels());
        categoryRecyclerAdapter.setCommunities(communityModels);

    }

    @Override
    public void onCategoryClicked(int id) {

        feedListView.initialLoading();
        currentSelectedSkillId = id;
        fetchPosts(id);

    }


    private void fetchPosts(int id) {

    }

    public void forceReloadData() {
    }

    private void loadMore(int id) {

        if (currentPostReponse == null)
            return;

        if (currentPostReponse.next.length() == 0) {
            return;
        }


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

    @Override
    public void onPostLiked(int postId) {
        L.D.m("Home Fragment", "liked the post");
    }

    @Override
    public void onPostLikeError() {
        L.D.m("Home Fragment", "unable to like the post");
    }

    //  CALLBACKS FROM FEED LIST VIEW

    @Override
    public void onRetryFeedLoading() {
        forceReloadData();
    }

    @Override
    public void onRefreshFeeds() {
        forceReloadData();
    }

    @Override
    public void onLoadMoreFeeds() {
        loadMore(currentSelectedSkillId);
    }

    @Override
    public void onHideCommunityList() {
        hideCategorySection();
    }

    @Override
    public void onShowCommunityList() {
        bringBackCategorySection();
    }


    //CALLBACKS FROM SERVICE WORKER

    @Override
    public void onFetchingFromServer() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onCacheLoadFailed() {

    }

    @Override
    public void onNoDataInCache() {

    }

    @Override
    public void onLoadedFromCache(ArrayList<Feed> cachedList) {

    }

    @Override
    public void onRefreshed(List<Object> refreshedList) {

    }

    @Override
    public void onRefreshFailed() {

    }

    @Override
    public void onAppendableDataLoaded(List<Object> appendableList) {

    }

    @Override
    public void onAppendableDataLoadingFailed() {

    }

    @Override
    public void onFeedsFetched(ArrayList<Feed> feeds) {
        feedListView.feedsRefreshed(feeds);
    }

    @Override
    public void onFetchingFromServerFailed() {

    }

}
