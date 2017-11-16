package com.hapramp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.activity.InfoEditingActivity;
import com.hapramp.adapters.ProfilePostAdapter;
import com.hapramp.adapters.ProfileSkillsRecyclerAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.interfaces.UserDpUpdateRequestCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.UserDpUpdateRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements FullUserDetailsCallback, ProfileSkillsRecyclerAdapter.OnCategoryItemClickListener, PostFetchCallback, UserDpUpdateRequestCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
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
    TextView bioTv;
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
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.trophies_count)
    TextView trophiesCount;
    @BindView(R.id.emptyMessage)
    TextView emptyMessage;
    @BindView(R.id.editBtn)
    TextView dpEditBtn;
    @BindView(R.id.dpProgressBar)
    ProgressBar dpProgressBar;
    @BindView(R.id.bio_divider)
    FrameLayout bioDivider;
    @BindView(R.id.bioEditorBtn)
    TextView bioEditorBtn;
    @BindView(R.id.scroller)
    ScrollView scroller;
    @BindView(R.id.contentProgress)
    ProgressBar contentProgress;
    private Context mContext;
    private ProfileSkillsRecyclerAdapter profileSkillsRecyclerAdapter;
    private ProfilePostAdapter profilePostAdapter;
    private FirebaseStorage storage;
    private Uri uploadedMediaUri;
    private boolean isMediaUploaded;
    private String dpUrl;
    private String bio = "";

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

        storage = FirebaseStorage.getInstance();

        profilePostAdapter = new ProfilePostAdapter(mContext);
        profileSkillsRecyclerAdapter = new ProfileSkillsRecyclerAdapter(mContext, this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        sectionsRv.setAdapter(profileSkillsRecyclerAdapter);

        profilePostRv.setLayoutManager(new LinearLayoutManager(mContext));
        profilePostRv.setAdapter(profilePostAdapter);
        profilePostRv.setNestedScrollingEnabled(false);
        bioEditorBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        dpEditBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        bioEditorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, InfoEditingActivity.class);
                i.putExtra("bio", bio);
                mContext.startActivity(i);
            }
        });
        dpEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void fetchUserDetails() {

        showContentLoadingProgress();
        showCategoryLoadingProgress();
        DataServer.getFullUserDetails(HaprampPreferenceManager.getInstance().getUserId(), this);

    }

    private void fetchUserProfilePosts(int skill_id) {

        if (skill_id == -1) {
            DataServer.getPosts(this);
        } else {
            DataServer.getPosts(skill_id, this);
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

        try {
            dpUrl = userModel.image_uri;
            profilePic.setImageURI(dpUrl);
            bio = userModel.bio != null ? userModel.bio : "";
            username.setText(userModel.username);
            hapname.setText("@hapname");
            bioTv.setText(bio);
            String _t = String.format(getResources().getString(R.string.profile_posts_count_caption), userModel.skills.size());
            postCounts.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_followers_caption), userModel.followers);
            followersCount.setText(_t);
            _t = String.format(getResources().getString(R.string.profile_following_count_caption), userModel.followings);
            followingsCount.setText(_t);
            hapcoinsCount.setText(String.valueOf(userModel.hapcoins));
            trophiesCount.setText("0");
            bindSkillsCategory(userModel.skills);

        } catch (Exception e) {

        }

        showContent(true);

    }

    private void showContent(boolean show) {

        try {
            if (show) {
                scroller.setVisibility(View.VISIBLE);
                contentProgress.setVisibility(View.GONE);
            } else {
                scroller.setVisibility(View.GONE);
                contentProgress.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

    }


    private void bindPosts(List<PostResponse> posts) {

        hideContentLoadingProgress();
        profilePostAdapter.setPostResponses(posts);

    }

    private void bindSkillsCategory(List<UserModel.Skills> skills) {

        hideCategoryLoadingProgress();
        skills.add(0, new UserModel.Skills(0, "All", "", ""));
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

    private void uploadMedia(String uri) {

        showDpUpdateProgress(true);
        StorageReference storageRef = storage.getReference();
        StorageReference dpRef = storageRef
                .child(Constants.userDpFolder)
                .child(HaprampPreferenceManager.getInstance().getUserId())
                .child(System.currentTimeMillis() + "_" + uri.substring(uri.lastIndexOf('/')));

        InputStream stream = null;
        L.D.m("Profile", "Uploading from..." + uri);

        try {
            stream = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showDpUpdateProgress(false);
        }

        UploadTask uploadTask = dpRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showDpUpdateProgress(false);
                Toast.makeText(mContext, "Failed To Upload Media", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                updateAppServerForDpUpdate(downloadUrl.toString());
                showDpUpdateProgress(false);
                L.D.m("Profile", " uploaded to : " + downloadUrl.toString());
            }
        });

    }

    private void updateAppServerForDpUpdate(String url) {

        DataServer.updataUserDpUrl(HaprampPreferenceManager.getInstance().getUserId(),
                new UserDpUpdateRequestBody(url),
                this);

    }

    private void showDpUpdateProgress(boolean show) {

        int vis = show ? View.VISIBLE : View.GONE;
        if (dpProgressBar != null) {
            dpProgressBar.setVisibility(vis);
        }

    }

    private void openGallery() {

        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_SELECTOR:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {
                    L.D.m("Profile", "Photo Url error!");
                } else {
                    profilePic.setImageURI(Uri.fromFile(new File(cursor.getString(columnIndex))));
                    uploadMedia(cursor.getString(columnIndex));
                }
                cursor.close();
            }
        }
    }


    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onCategoryClicked(int id) {

        // fetch the selected posts
        fetchUserProfilePosts(id);

    }

    @Override
    public void onPostFetched(List<PostResponse> postResponses) {
        if (postResponses.size() > 0) {
            showEmptyMessage(false);
        } else {
            showEmptyMessage(true);
        }
        bindPosts(postResponses);
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


    @Override
    public void onUserDpUpdated() {
        showDpUpdateProgress(false);
    }

    @Override
    public void onUserDpUpdateFailed() {
        profilePic.setImageURI(dpUrl);
        showDpUpdateProgress(false);
        // revert back the pic
    }
}
