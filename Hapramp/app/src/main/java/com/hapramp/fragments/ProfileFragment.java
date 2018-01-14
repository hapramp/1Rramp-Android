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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.activity.CommentEditorActivity;
import com.hapramp.activity.DetailedActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.activity.ProfileEditActivity;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.SpaceDecorator;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements
        FullUserDetailsCallback,
        PostFetchCallback {


    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    private Context mContext;

    private PostsRecyclerAdapter profilePostAdapter;
    private String dpUrl;
    private String mBio = "";
    private ViewItemDecoration viewItemDecoration;
    private Unbinder unbinder;
    private String _t;
    private LinearLayoutManager llm;
    private PostResponse currentPostReponse;

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
        init();
        fetchUserDetails();
        // start loading with given default limits
        fetchUserProfilePosts(URLS.POST_FETCH_START_URL);
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        try {
            if (currentPostReponse.next.length() == 0) {
                return;
            }

            fetchUserProfilePosts(currentPostReponse.next);

        }catch (Exception e){

            Log.d("ProfileFragment",e.toString());

        }

    }

    private void init() {

        profilePostAdapter = new PostsRecyclerAdapter(mContext);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        SpaceDecorator spaceDecorator = new SpaceDecorator();
        profilePostRv.addItemDecoration(spaceDecorator);
        profilePostRv.addItemDecoration(viewItemDecoration);
        llm = new LinearLayoutManager(mContext);
        profilePostRv.setLayoutManager(llm);
        profilePostRv.setAdapter(profilePostAdapter);
        setScrollListener();

    }

    private void fetchUserDetails() {

        DataServer.getFullUserDetails(HaprampPreferenceManager.getInstance().getUserId(), this);

    }

    private void fetchUserProfilePosts(String url) {

        // get all post of this user
        DataServer.getPostsByUserId(url, Integer.valueOf(HaprampPreferenceManager.getInstance().getUserId()), this);

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

        ProfileHeaderModel profileHeaderModel = new ProfileHeaderModel(
                userModel.id,
                userModel.image_uri,
                userModel.username,
                "",
                true,
                userModel.bio,
                0,
                userModel.followers,
                userModel.followings,
                userModel.skills);


        profilePostAdapter.setProfileHeaderModel(profileHeaderModel);

        showContent(true);

    }

    private void showContent(boolean show) {
        if(show){
            //hide progress bar
            if(contentLoadingProgress!=null){
                contentLoadingProgress.setVisibility(View.GONE);
            }
        }
    }

    private void bindPosts(List<PostResponse.Results> posts) {

        profilePostAdapter.appendResult(posts);

    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onPostFetched(PostResponse postResponses) {

        currentPostReponse = postResponses;
        profilePostAdapter.setHasMoreToLoad(postResponses.next.length()>0);
        bindPosts(postResponses.results);

    }


    @Override
    public void onPostFetchError() {
        Toast.makeText(mContext, "Error Fetching Your Posts...", Toast.LENGTH_SHORT).show();
    }


}
