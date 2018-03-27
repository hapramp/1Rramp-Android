package com.hapramp.fragments;

import android.content.Context;
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
import android.widget.ProgressBar;

import com.hapramp.R;
import com.hapramp.adapters.ProfileRecyclerAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
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
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;
    private Context mContext;
    private ProfileRecyclerAdapter profilePostAdapter;
    private ViewItemDecoration viewItemDecoration;
    private Unbinder unbinder;
    private LinearLayoutManager llm;

    private String username;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = HaprampPreferenceManager.getInstance().getSteemUsername();
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
        return view;

    }

    public void reloadPosts() {
        fetchUserProfilePosts(URLS.POST_FETCH_START_URL);
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

        profilePostAdapter = new ProfileRecyclerAdapter(mContext);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(false);
        profilePostRv.addItemDecoration(viewItemDecoration);
        llm = new LinearLayoutManager(mContext);
        profilePostRv.setLayoutManager(llm);
        profilePostRv.setAdapter(profilePostAdapter);
        setScrollListener();

    }

    private void fetchUserProfilePosts(String url) {

        // get all post of this user
        //DataServer.getPostsByUserId(url, Integer.valueOf(HaprampPreferenceManager.getInstance().getUserId()), this);

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

    private void showContent(boolean show) {

        if (show) {
            //hide progress bar
            if (contentLoadingProgress != null) {
                contentLoadingProgress.setVisibility(View.GONE);
            }
        }

    }

    private void fetchUserDataFromSteem() {

        String user_api_url = String.format(
                mContext.getResources().getString(R.string.steem_user_api),
                HaprampPreferenceManager.getInstance().getSteemUsername());

        RetrofitServiceGenerator.getService()
                .getSteemUser(user_api_url)
                .enqueue(new Callback<SteemUser>() {
                    @Override
                    public void onResponse(Call<SteemUser> call, Response<SteemUser> response) {
                        //populate User Info
                        if(response.isSuccessful()) {
                            bindSteemData(response.body());
                        }else{
                            failedToFetchSteemInfo();
                        }
                    }

                    @Override
                    public void onFailure(Call<SteemUser> call, Throwable t) {
                        failedToFetchSteemInfo();
                    }
                });

    }

    private void failedToFetchSteemInfo() {

    }

    private void bindSteemData(SteemUser steemUser) {

    }

    private void fetchUserProfilePosts() {

        RetrofitServiceGenerator.getService()
                .getPostsOfUser(username,POST_LIMIT)
                .enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                if(response.isSuccessful()){
                    bindProfilePosts(response.body());
                }else{
                    failedToFetchUserPosts();
                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {
                failedToFetchUserPosts();
            }
        });

    }

    private void failedToFetchUserPosts() {

    }

    private void bindProfilePosts(List<Feed> body) {

    }

}
