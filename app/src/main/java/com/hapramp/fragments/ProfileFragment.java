package com.hapramp.fragments;

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
import android.widget.ProgressBar;

import com.hapramp.R;
import com.hapramp.adapters.ProfileRecyclerAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.FeedResponse;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {


    private static final int POST_LIMIT = 100;
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;

    private Context mContext;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private Unbinder unbinder;
    private LinearLayoutManager llm;

    private String username;

    private static final String TAG = ProfileFragment.class.getSimpleName();

    public ProfileFragment() {
        // Required empty public constructor
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
        fetchUserProfilePosts();
        return view;

    }

    public void reloadPosts() {

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

//        try {
//            if (currentPostReponse.next.length() == 0) {
//                return;
//            }
//
//            fetchUserProfilePosts(currentPostReponse.next);
//
//        }catch (Exception e){
//
//            Log.d("ProfileFragment",e.toString());
//
//        }

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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fetchUserProfilePosts() {

        Log.d("ProfilePost", Profile.getDefaultProfileAsJson());

        RetrofitServiceGenerator.getService()
                .getPostsOfUser(username, POST_LIMIT)
                .enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        if (response.isSuccessful()) {
                            bindProfilePosts(response.body());
                        } else {
                            failedToFetchUserPosts();
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        failedToFetchUserPosts();
                    }
                });

    }

    private void failedToFetchUserPosts() {

    }

    private void bindProfilePosts(FeedResponse body) {

        Log.d("ProfileFragment", " posts " + body.getFeeds().size());
        //Profile.fetchUserProfilesFor(body);
        profilePostAdapter.setPosts(body.getFeeds());

    }

}
