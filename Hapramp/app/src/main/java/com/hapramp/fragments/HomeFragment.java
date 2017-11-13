package com.hapramp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.CategoryRecyclerAdapter;
import com.hapramp.PostsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.activity.DetailedPostActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.LikePostCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.SkillsModel;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PostFetchCallback, FetchSkillsResponse, CategoryRecyclerAdapter.OnCategoryItemClickListener, PostsRecyclerAdapter.OnPostElementsClickListener, LikePostCallback {

    @BindView(R.id.homeRv)
    RecyclerView homeRv;
    Unbinder unbinder;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;
    @BindView(R.id.categoryLoadingProgress)
    ProgressBar categoryLoadingProgress;
    private PostsRecyclerAdapter recyclerAdapter;
    private Context mContext;
    private int category;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new PostsRecyclerAdapter(mContext);
        recyclerAdapter.setPostElementsClickListener(this);
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
        homeRv.setLayoutManager(new LinearLayoutManager(mContext));
        homeRv.setAdapter(recyclerAdapter);
        fetchCategories();
        fetchPosts(0);
    }

    private void initCategoryView() {

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(categoryRecyclerAdapter);

    }

    @Override
    public void onCategoryClicked(int id) {

        L.D.m("Category", " clicked :"+id);
        fetchPosts(id);

    }

    private void fetchCategories() {
        showCategoryLoadingProgress();
        DataServer.fetchSkills(this);
    }

    @Override
    public void onSkillsFetched(List<SkillsModel> skillsModels) {
        hideCategoryLoadingProgress();
        skillsModels.add(0,new SkillsModel(0,"All","",""));
        categoryRecyclerAdapter.setCategories(skillsModels);
    }

    @Override
    public void onSkillFetchError() {
        hideCategoryLoadingProgress();
    }


    private void showContentLoadingProgress() {
        if (contentLoadingProgress != null)
            contentLoadingProgress.setVisibility(View.VISIBLE);
    }

    private void hideContentLoadingProgress() {
        if (contentLoadingProgress != null)
            contentLoadingProgress.setVisibility(View.GONE);
    }

    private void showCategoryLoadingProgress() {
        if (categoryLoadingProgress != null)
            categoryLoadingProgress.setVisibility(View.VISIBLE);
    }

    private void hideCategoryLoadingProgress() {
        if (categoryLoadingProgress != null)
            categoryLoadingProgress.setVisibility(View.GONE);
    }


    private void fetchPosts(int id) {

        hideErrorMessage();
        hideContent();
        showContentLoadingProgress();

        if (id == 0) {
            DataServer.getPosts(this);
        } else {
            DataServer.getPosts(id, this);
        }

    }

    @Override
    public void onPostFetched(List<PostResponse> postResponses) {
        hideContentLoadingProgress();
        if (postResponses.size() > 0) {
            hideErrorMessage();
            showContent();
            recyclerAdapter.setPostResponses(postResponses);
        } else {
            showErrorMessage();
            hideContent();
        }

    }

    private void showContent() {
        if(homeRv!=null)
            homeRv.setVisibility(View.VISIBLE);
    }

    private void hideContent() {

        if(homeRv!=null)
            homeRv.setVisibility(View.GONE);
    }

    private void showErrorMessage(){
        if (emptyMessage != null)
            emptyMessage.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage(){
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
    public void onReadMoreTapped(PostResponse postResponse) {
        Intent intent = new Intent(mContext, DetailedPostActivity.class);
        intent.putExtra("username",postResponse.getUser().getFull_name());
        intent.putExtra("mediaUri",postResponse.getMedia_uri());
        intent.putExtra("content",postResponse.getContent());
        intent.putExtra("postId",String.valueOf(postResponse.getId()));
        mContext.startActivity(intent);
    }

    @Override
    public void onPostLiked(int postId) {
        L.D.m("Home Fragment","liked the post");
    }

    @Override
    public void onPostLikeError() {
        L.D.m("Home Fragment","unable to like the post");
    }

}
