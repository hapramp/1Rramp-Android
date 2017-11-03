package com.hapramp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.CategoryRecyclerAdapter;
import com.hapramp.PostsRecyclerAdapter;
import com.hapramp.ProfilePostAdapter;
import com.hapramp.ProfileSkillsRecyclerAdapter;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.models.response.SkillsModel;
import com.hapramp.models.response.UserModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements FullUserDetailsCallback, ProfileSkillsRecyclerAdapter.OnCategoryItemClickListener {

    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @BindView(R.id.follow_btn)
    TextView followBtn;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.divider_top)
    FrameLayout dividerTop;
    @BindView(R.id.post_counts)
    TextView postCounts;
    @BindView(R.id.followers_count)
    TextView followersCount;
    @BindView(R.id.followings_count)
    TextView followingsCount;
    @BindView(R.id.post_stats)
    LinearLayout postStats;
    @BindView(R.id.divider_bottom)
    FrameLayout dividerBottom;
    @BindView(R.id.badge_containers)
    LinearLayout badgeContainers;
    @BindView(R.id.recentPostsCaption)
    TextView recentPostsCaption;
    Unbinder unbinder;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;
    @BindView(R.id.categoryLoadingProgress)
    ProgressBar categoryLoadingProgress;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    private Context mContext;
    private ProfileSkillsRecyclerAdapter profileSkillsRecyclerAdapter;
    private ProfilePostAdapter profilePostAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        initCategoryView();
        profilePostRv.setLayoutManager(new LinearLayoutManager(mContext));
        profilePostRv.setAdapter(profilePostAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserDetails();
    }

    private void initCategoryView() {

        profilePostAdapter = new ProfilePostAdapter(mContext);
        profileSkillsRecyclerAdapter = new ProfileSkillsRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(profileSkillsRecyclerAdapter);

    }


    private void fetchUserDetails() {
        showContentLoadingProgress();
        showCategoryLoadingProgress();
        DataServer.getFullUserDetails("26", this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onFullUserDetailsFetched(UserModel userModel) {

        try {
            profilePic.setImageURI(userModel.getImage_uri());
            username.setText(userModel.getUsername());
            hapname.setText("@hapname");
            bio.setText("-----Bio----- Here-----");
            bindSkillsCategory(userModel.getSkills());
            bindPosts(userModel.getPosts());
        } catch (Exception e) {

        }

    }

    private void bindPosts(List<UserModel.Posts> posts) {
        hideContentLoadingProgress();
        profilePostAdapter.setPostResponses(posts);
    }

    private void bindSkillsCategory(List<UserModel.Skills> skills) {
        hideCategoryLoadingProgress();
        profileSkillsRecyclerAdapter.setCategories(skills);
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


    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onCategoryClicked(int id) {

    }

}
