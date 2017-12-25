package com.hapramp.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.adapters.CategoryRecyclerAdapter;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.activity.DetailedPostActivity;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.SkillsModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ViewItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements PostFetchCallback, FetchSkillsResponse, CategoryRecyclerAdapter.OnCategoryItemClickListener, PostsRecyclerAdapter.postListener, LikePostCallback {

    @BindView(R.id.homeRv)
    RecyclerView postsRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.loadingShimmer)
    View loadingShimmer;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;

    private PostsRecyclerAdapter recyclerAdapter;
    private Context mContext;
    private PostResponse currentPostReponse;
    private int currentSelectedSkillId;

    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ViewItemDecoration viewItemDecoration;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initCategoryView();
        fetchPosts(0);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(mContext);
        postsRecyclerView.setLayoutManager(layoutManager);

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);

        recyclerAdapter = new PostsRecyclerAdapter(mContext, postsRecyclerView);
        recyclerAdapter.setListener(this);
        postsRecyclerView.addItemDecoration(viewItemDecoration);
        postsRecyclerView.setAdapter(recyclerAdapter);
        postsRecyclerView.setNestedScrollingEnabled(false);
        attachListeners();

    }

    private void initCategoryView() {

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(categoryRecyclerAdapter);

        List<SkillsModel> skillsModels = SkillsModel.marshelSkills(HaprampPreferenceManager.getInstance().getUser().skills);
        skillsModels.add(0, new SkillsModel(0, "All", "", ""));
        categoryRecyclerAdapter.setCategories(skillsModels);
    }

    private void attachListeners() {

//        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
//
//        postsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();
//
//                if(currentFirstVisible > firstVisibleInListview) {
//                    // Scrolling Up: Hide the Category Section
//                    sectionsRv.animate().translationY(-100).start();
//                }else {
//                    // Scrolling Up: Show the Category Section
//                    sectionsRv.animate().translationY(0).start();
//                }
//
//            }
//        });
    }

    @Override
    public void onCategoryClicked(int id) {

        currentSelectedSkillId = id;
        fetchPosts(id);

    }

    @Override
    public void onSkillsFetched(List<SkillsModel> skillsModels) {
        skillsModels.add(0, new SkillsModel(0, "All", "", ""));
        categoryRecyclerAdapter.setCategories(skillsModels);
    }

    @Override
    public void onSkillFetchError() {

    }

    private void showContentLoadingProgress() {
        loadingShimmer.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();

    }

    private void hideContentLoadingProgress() {
        if(loadingShimmer!=null)
            loadingShimmer.setVisibility(View.GONE);
    }


    private void fetchPosts(int id) {

        hideErrorMessage();
        hideContent();
        showContentLoadingProgress();

        if (id == 0) {
            DataServer.getPosts(URLS.POST_FETCH_START_URL, this);
        } else {
            DataServer.getPosts(URLS.POST_FETCH_START_URL, id, this);
        }

    }

    private void loadMore(int id) {

        if (currentPostReponse.next.length() == 0)
            return;

        if (id == 0) {
            DataServer.getPosts(currentPostReponse.next, this);
        } else {
            DataServer.getPosts(currentPostReponse.next, id, this);
        }

    }

    @Override
    public void onPostFetched(PostResponse postResponses) {

        //todo: Don`t reverse the list. It should be modified by server end
        currentPostReponse = postResponses;
        Collections.reverse(postResponses.results);
        // append Result
        if (postResponses.results.size() > 0) {
            hideErrorMessage();
            showContent();
            recyclerAdapter.appendResult(postResponses.results);
        } else {
            showErrorMessage();
            hideContent();
        }

        hideContentLoadingProgress();

    }

    private void showContent() {
        if (postsRecyclerView != null)
            postsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideContent() {

        if (postsRecyclerView != null)
            postsRecyclerView.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        if (emptyMessage != null)
            emptyMessage.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        if (emptyMessage != null)
            emptyMessage.setVisibility(View.GONE);
    }

    @Override
    public void onPostFetchError() {
        hideContentLoadingProgress();
        Toast.makeText(mContext, "Error Loading Content. Inconvienience is regreted :(", Toast.LENGTH_LONG).show();
        L.D.m("HomeFragment", "Fetch Error: Post");
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
    public void onReadMoreTapped(PostResponse.Results postResponse) {

        Intent intent = new Intent(mContext, DetailedPostActivity.class);
        intent.putExtra("isVoted", postResponse.is_voted);
        intent.putExtra("vote", postResponse.current_vote);
        intent.putExtra("username", postResponse.user.username);
        intent.putExtra("mediaUri", postResponse.media_uri);
        intent.putExtra("content", postResponse.content);
        intent.putExtra("postId", String.valueOf(postResponse.id));
        intent.putExtra("userDpUrl", postResponse.user.image_uri);
        intent.putExtra("totalVoteSum", String.valueOf(postResponse.vote_sum));
        intent.putExtra("totalUserVoted",String.valueOf(postResponse.vote_count));

        mContext.startActivity(intent);
    }

    @Override
    public void onUserInfoTapped(int userId) {
        // redirect to profile page
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);

    }

    @Override
    public void onLoadMore() {
        loadMore(currentSelectedSkillId);
    }

    @Override
    public void onOverflowIconTapped(View view, int postId, int position) {
        // do nothing...
    }

    @Override
    public void onPostLiked(int postId) {
        L.D.m("Home Fragment", "liked the post");
    }

    @Override
    public void onPostLikeError() {
        L.D.m("Home Fragment", "unable to like the post");
    }

    public void forceReloadData() {
        fetchPosts(0);
    }
}
