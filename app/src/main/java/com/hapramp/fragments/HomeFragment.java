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
import android.widget.AbsListView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CategoryRecyclerAdapter;
import com.hapramp.adapters.HomeFeedsAdapter;
import com.hapramp.api.URLS;
import com.hapramp.datastore.HomeDataManager;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.SpaceDecorator;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.feedlist.FeedListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements FetchSkillsResponse,
        CategoryRecyclerAdapter.OnCategoryItemClickListener, LikePostCallback,
        HomeDataManager.PostLoadListener, FeedListView.FeedListViewListener {

    @BindView(R.id.feedListView)
    FeedListView feedListView;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;

    private Context mContext;
    private PostResponse currentPostReponse;
    private int currentSelectedSkillId;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    HomeDataManager dataManager;
    private Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = new HomeDataManager(getActivity());
        dataManager.registerPostListeners(this);

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

        List<UserModel.Skills> skillsModels = HaprampPreferenceManager.getInstance().getUser().skills;
        skillsModels.add(0, new UserModel.Skills(0, "All", "", ""));
        categoryRecyclerAdapter.setCategories(skillsModels);

    }

    @Override
    public void onCategoryClicked(int id) {

        feedListView.initialLoading();
        currentSelectedSkillId = id;
        fetchPosts(id);

    }

    @Override
    public void onSkillsFetched(List<UserModel.Skills> skillsModels) {
        skillsModels.add(0, new UserModel.Skills(0, "All", "", ""));
        categoryRecyclerAdapter.setCategories(skillsModels);
    }

    @Override
    public void onSkillFetchError() {

    }

    private void fetchPosts(int id) {

        dataManager.getPosts(URLS.POST_FETCH_START_URL, id);

    }

    public void forceReloadData() {
        // dataManager.getPosts(URLS.POST_FETCH_START_URL, currentSelectedSkillId, false);
        dataManager.getFreshPosts(URLS.POST_FETCH_START_URL, currentSelectedSkillId);

    }

    private void loadMore(int id) {

        if (currentPostReponse == null)
            return;

        if (currentPostReponse.next.length() == 0) {
            return;
        }

        dataManager.getPostForLoadMoreRequest(currentPostReponse.next, id);

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

    //CALLBACKS FROM DATA MANAGER
    @Override
    public void onLoadingFromCache() {
    }

    @Override
    public void onFeedLoadedFromCache(PostResponse response) {

        if(feedListView!=null) {
            currentPostReponse = response;
            feedListView.cachedFeedFetched(response.results);
        }


    }

    @Override
    public void onNoFeedFoundInCache() {

        if(feedListView!=null) {
            feedListView.noCachedFeeds();
        }

    }

    @Override
    public void onRefreshingPostFromServer() {

        if(feedListView!=null) {
            feedListView.feedRefreshing();
        }

    }

    @Override
    public void onFreshFeedsFechted(PostResponse postResponse) {

        if(feedListView!=null) {
            currentPostReponse = postResponse;
            feedListView.setHasMoreToLoad(currentPostReponse.next.length() > 0);
            feedListView.feedsRefreshed(postResponse.results);
        }

    }

    @Override
    public void onFreshFeedFetchError(String msg) {

        if(feedListView!=null) {
            feedListView.failedToRefresh(msg);
        }

    }

    @Override
    public void onLoadingPostForAppending() {

    }

    @Override
    public void onFeedLoadedForAppending(PostResponse response) {

        if(feedListView!=null) {
            currentPostReponse = response;
            feedListView.loadedMoreFeeds(response.results);
        }

    }

    @Override
    public void onNoFeedForAppending() {

    }

    @Override
    public void onAppendingFeedLoadError(String msg) {

        try {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }

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

}
