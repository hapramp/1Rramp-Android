package com.hapramp.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.adapters.CategoryRecyclerAdapter;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.api.URLS;
import com.hapramp.datastore.DataManager;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.PixelUtils;
import com.hapramp.utils.SpaceDecorator;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements FetchSkillsResponse,
        CategoryRecyclerAdapter.OnCategoryItemClickListener, LikePostCallback,
        DataManager.PostLoadListener {

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
    @BindView(R.id.homeRefressLayout)
    SwipeRefreshLayout homeRefressLayout;


    private PostsRecyclerAdapter recyclerAdapter;
    private Context mContext;
    private PostResponse currentPostReponse;
    private int currentSelectedSkillId;

    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ViewItemDecoration viewItemDecoration;
    private int y;
    DataManager dataManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        dataManager = new DataManager(getActivity());
        dataManager.registerPostListeners(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HomeFragment", "onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        layoutManager = new LinearLayoutManager(mContext);
        postsRecyclerView.setLayoutManager(layoutManager);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        SpaceDecorator spaceDecorator = new SpaceDecorator();
        postsRecyclerView.addItemDecoration(spaceDecorator);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        postsRecyclerView.setLayoutAnimation(animation);

        recyclerAdapter = new PostsRecyclerAdapter(mContext);
        recyclerAdapter.setIsAdapterForProfile(false);
        postsRecyclerView.addItemDecoration(viewItemDecoration);
        postsRecyclerView.setAdapter(recyclerAdapter);
        postsRecyclerView.setNestedScrollingEnabled(false);
        fetchPosts(0);
        setScrollListener();
        homeRefressLayout.setProgressViewOffset(false, PixelUtils.dpToPx(72),PixelUtils.dpToPx(120));
        homeRefressLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                homeRefressLayout.setEnabled(true);
//                homeRefressLayout.setRefreshing(true);

                forceReloadData();
            }
        });

    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {

        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void onPostLoaded(PostResponse postResponses) {

        hideContentLoadingProgress();
        currentPostReponse = postResponses;
        // append Result
        if (postResponses.results.size() > 0) {
            hideErrorMessage();
            showContent();

            recyclerAdapter.setHasMoreToLoad(postResponses.next.length() > 0);
            recyclerAdapter.appendResult(postResponses.results);

        } else {
            showErrorMessage();
            hideContent();
        }

    }

    @Override
    public void onPostLoadError(String errorMsg) {
    }

    @Override
    public void onLoading() {

        showContentLoadingProgress();
        hideContent();
        hideErrorMessage();

    }

    @Override
    public void onRefreshing() {
        if(!homeRefressLayout.isRefreshing()) {
            homeRefressLayout.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("HomeFragment","refressing");
                    homeRefressLayout.setEnabled(false);
                    homeRefressLayout.setRefreshing(true);
                }
            });
        }
    }


    @Override
    public void onPostRefreshed(PostResponse refreshedResponse) {
        Log.d("HomeFragment","refreshed!!");
        homeRefressLayout.setRefreshing(false);
        homeRefressLayout.setEnabled(true);

        currentPostReponse = refreshedResponse;
        recyclerAdapter.setPosts(refreshedResponse.results);
        runLayoutAnimation(postsRecyclerView);
        showContent();
        hideContentLoadingProgress();
        Toast.makeText(mContext, "Reloaded", Toast.LENGTH_SHORT).show();

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

            y = dy;

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                if (y > 0) {
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

    private void setScrollListener() {
        postsRecyclerView.addOnScrollListener(new EndlessOnScrollListener(layoutManager) {
            @Override
            public void onScrolledToEnd() {
                loadMore(currentSelectedSkillId);
            }
        });
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
        recyclerAdapter.clearList();
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
        if (loadingShimmer != null) {
            loadingShimmer.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
        }
    }

    private void hideContentLoadingProgress() {
        if (loadingShimmer != null)
            loadingShimmer.setVisibility(View.GONE);
    }

    private void fetchPosts(int id) {

        dataManager.getPosts(URLS.POST_FETCH_START_URL, id, false);

    }

    public void forceReloadData() {
        dataManager.getPosts(URLS.POST_FETCH_START_URL, currentSelectedSkillId, false);
    }

    private void loadMore(int id) {

        if (currentPostReponse.next.length() == 0) {
            return;
        }

        dataManager.getPosts(currentPostReponse.next, id, true);

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

}
