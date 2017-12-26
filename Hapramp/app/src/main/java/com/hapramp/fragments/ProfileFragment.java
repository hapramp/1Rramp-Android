package com.hapramp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.activity.DetailedPostActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.activity.ProfileEditActivity;
import com.hapramp.adapters.PostsRecyclerAdapter;
import com.hapramp.adapters.ProfileSkillsRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.api.URLS;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.OnPostDeleteCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.interfaces.UserDpUpdateRequestCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.UserDpUpdateRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements
        FullUserDetailsCallback,
        PostFetchCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @BindView(R.id.edit_btn)
    TextView editBtn;
    @BindView(R.id.bio)
    TextView bioTextView;
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
    @BindView(R.id.interestCaption)
    TextView interestCaption;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;
    @BindView(R.id.postsCaption)
    TextView postsCaption;
    @BindView(R.id.profilePostRv)
    RecyclerView profilePostRv;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.scroller)
    ScrollView scroller;
    @BindView(R.id.contentLoadingProgress)
    ProgressBar contentLoadingProgress;

    private Context mContext;
    private ProfileSkillsRecyclerAdapter profileSkillsRecyclerAdapter;
    private PostsRecyclerAdapter profilePostAdapter;
    private FirebaseStorage storage;
    private Uri uploadedMediaUri;
    private boolean isMediaUploaded;
    private String dpUrl;
    private String bio = "";
    private ViewItemDecoration viewItemDecoration;
    private Unbinder unbinder;
    private String _t;

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

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserDetails();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserProfilePosts(0);

    }

    private void init() {

        profilePostAdapter = new PostsRecyclerAdapter(mContext, profilePostRv);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);

        profilePostRv.addItemDecoration(viewItemDecoration);

        // profilePostAdapter.setPostItemActionListener(this);
        profileSkillsRecyclerAdapter = new ProfileSkillsRecyclerAdapter(mContext);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(profileSkillsRecyclerAdapter);

        profilePostRv.setLayoutManager(new LinearLayoutManager(mContext));
        profilePostRv.setAdapter(profilePostAdapter);
        profilePostRv.setNestedScrollingEnabled(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProfileEditActivity.class);
                mContext.startActivity(i);
            }
        });

    }

    private void fetchUserDetails() {

        DataServer.getFullUserDetails(HaprampPreferenceManager.getInstance().getUserId(), this);

    }

    private void fetchUserProfilePosts(int skill_id) {

        if (skill_id == -1) {
            // get all post of this user
            DataServer.getPostsByUserId(URLS.POST_FETCH_START_URL, Integer.valueOf(HaprampPreferenceManager.getInstance().getUserId()), this);
        } else {
            // get post by user and skills
            DataServer.getPosts(URLS.POST_FETCH_START_URL, skill_id, Integer.valueOf(HaprampPreferenceManager.getInstance().getUserId()), this);

        }

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

        dpUrl = userModel.image_uri;
        // profilePic.setImageURI(dpUrl);
        ImageHandler.loadCircularImage(mContext, profilePic, dpUrl);
        bio = userModel.bio != null ? userModel.bio : "";
        username.setText(userModel.username);
        hapname.setText("@hapname");
        bioTextView.setText(bio);
//            String _t = String.format(getResources().getString(R.string.profile_posts_count_caption), );
//            postCounts.setText(_t);
        _t = String.format(getResources().getString(R.string.profile_followers_caption), userModel.followers);
        followersCount.setText(_t);
        _t = String.format(getResources().getString(R.string.profile_following_count_caption), userModel.followings);
        followingsCount.setText(_t);
        bindSkillsCategory(userModel.skills);


        showContent(true);

    }

    private void showContent(boolean show) {

        try {
            if (show) {
                scroller.setVisibility(View.VISIBLE);
                contentLoadingProgress.setVisibility(View.GONE);
            } else {
                scroller.setVisibility(View.GONE);
                contentLoadingProgress.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

    }

    private void bindPosts(List<PostResponse.Results> posts) {

        profilePostAdapter.appendResult(posts);

    }

    private void bindSkillsCategory(List<UserModel.Skills> skills) {

        profileSkillsRecyclerAdapter.setCategories(skills);

    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onPostFetched(PostResponse postResponses) {

        if (postResponses.results.size() > 0) {
            showEmptyMessage(false);
        } else {
            showEmptyMessage(true);
        }
        bindPosts(postResponses.results);
    }

    private void showEmptyMessage(boolean show) {
        int vis = show ? View.VISIBLE : View.GONE;
        if (emptyMessage != null) {
            emptyMessage.setVisibility(vis);
        }
    }

    @Override
    public void onPostFetchError() {
        Toast.makeText(mContext, "Error Fetching Your Posts...", Toast.LENGTH_SHORT).show();
    }


}
