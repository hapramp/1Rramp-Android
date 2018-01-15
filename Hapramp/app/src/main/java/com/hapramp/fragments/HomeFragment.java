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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.activity.CommentEditorActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.adapters.CategoryRecyclerAdapter;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.activity.DetailedActivity;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.SpaceDecorator;
import com.hapramp.utils.ViewItemDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements PostFetchCallback, FetchSkillsResponse, CategoryRecyclerAdapter.OnCategoryItemClickListener,LikePostCallback {

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
    private int y;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("HomeFragment","onCreate "+savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HomeFragment","onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("HomeFragment","onDestroy");
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

        public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

        // use your LayoutManager instead
        private LinearLayoutManager lm;

        EndlessOnScrollListener(LinearLayoutManager llm) {
            this.lm = llm;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //super.onScrolled(recyclerView, dx, dy);

            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }

            y=dy;

        }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    if(y>0){
                        hideCategorySection();
                    } else {
                        bringBackCategorySection();
                    }
                }
            }

            public abstract void onScrolledToEnd();

    }

    private void bringBackCategorySection() {
        sectionsRv.animate().translationY(0);
    }

    private void hideCategorySection() {
        sectionsRv.animate().translationY(-sectionsRv.getMeasuredHeight());
    }

    private void setScrollListener(){
        postsRecyclerView.addOnScrollListener(new EndlessOnScrollListener(layoutManager) {
            @Override
            public void onScrolledToEnd() {
                loadMore(currentSelectedSkillId);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(mContext);
        postsRecyclerView.setLayoutManager(layoutManager);

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        SpaceDecorator spaceDecorator = new SpaceDecorator();
        postsRecyclerView.addItemDecoration(spaceDecorator);
        recyclerAdapter = new PostsRecyclerAdapter(mContext);
        postsRecyclerView.addItemDecoration(viewItemDecoration);
        postsRecyclerView.setAdapter(recyclerAdapter);
        postsRecyclerView.setNestedScrollingEnabled(false);

        setScrollListener();
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

    private void showContentLoadingProgress() {
        if(loadingShimmer!=null) {
            loadingShimmer.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
        }
    }

    private void hideContentLoadingProgress() {
        if (loadingShimmer != null)
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

        if (currentPostReponse.next.length() == 0){
            return;
        }

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
        // append Result
        if (postResponses.results.size() > 0) {
            hideErrorMessage();
            showContent();

            recyclerAdapter.setHasMoreToLoad(postResponses.next.length()>0);
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
        fetchPosts(currentSelectedSkillId);
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
