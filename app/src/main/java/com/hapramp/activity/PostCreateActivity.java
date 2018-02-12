package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.controller.PostCreationController;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.PostJobModel;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FileUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.post.PostCategoryView;
import com.hapramp.views.post.PostImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostCreateActivity extends AppCompatActivity implements PostCreateCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.postButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.characterLimit)
    TextView characterLimit;
    @BindView(R.id.postImageView)
    PostImageView postImageView;
    @BindView(R.id.category_caption)
    TextView categoryCaption;
    @BindView(R.id.postCategoryView)
    PostCategoryView postCategoryView;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.photosBtn)
    TextView photosBtn;
    @BindView(R.id.audioBtn)
    TextView audioBtn;
    @BindView(R.id.videoBtn)
    TextView videoBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_creation);
        ButterKnife.bind(this);
        init();
        initProgressDialog();
        attachListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        HaprampPreferenceManager.getInstance().savePostDraft(content.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    //    loadDraft();
    }
//
//    private void loadDraft() {
//
////        //load content
////        content.setText(HaprampPreferenceManager.getInstance().getPostDraft());
////        //load image
////        if (HaprampPreferenceManager.getInstance().getPostMediaDraft().length() > 0) {
////            loadImageIntoView(HaprampPreferenceManager.getInstance().getPostMediaDraft());
////        }
//
//    }
//
//    private void clearDraft() {
//        // clear post content
//        HaprampPreferenceManager.getInstance().savePostDraft("");
//        // clear image draft
//        HaprampPreferenceManager.getInstance().savePostMediaDraft("");
//
//    }

    private void init() {

        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        photosBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        audioBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        videoBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        postCategoryView.setCategoryItems(SkillsUtils.getSkillsSet());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void attachListener() {

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterLimit.setText(String.format(getResources().getString(R.string.post_limit), s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishPost();
            }
        });


        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Coming Soon! We Apreciate Your Patience :)");
                //todo: Implement audio Upload
//                galleryType = "audio/*";
//                openGallery();
            }
        });

        photosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Coming Soon!  We Apreciate Your Patience :)");
                //todo :  Implement video feature
//                galleryType = "video/*";
//                openGallery();
            }
        });

    }

    private void publishPost() {

        // check connection
        if (!ConnectionUtils.isConnected(this)) {
            showConnectivityError();
            return;
        }

        if (postCategoryView.getSelectedSkills().size() == 0) {
            toast("Select Atleast One Category");
            return;
        }

        String mediaUri = postImageView.getDownloadUrl();

        if (mediaUri != null) {

            showPublishingProgressDialog(true);

            PostCreateBody postCreateBody = new PostCreateBody(
                    content.getText().toString(),
                    postImageView.getDownloadUrl(),
                    Constants.CONTENT_TYPE_POST,
                    postCategoryView.getSelectedSkills(),
                    null);

            showPublishingProgressDialog(true);
            //gather the data
            DataServer.createPost(postCreateBody, this);
        } else {
            toast("Your media has not been uploaded yet");
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void initProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Post Upload");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading Your Post...");

    }

    private void showConnectivityError() {
        Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
    }

    private void showPublishingProgressDialog(boolean show) {

        if (progressDialog != null) {
            if (show) {
                progressDialog.setMessage("Publishing Your Post");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        } else {
            progressDialog.hide();
        }

    }

    private void openGallery() {

        try {
            if (ActivityCompat.checkSelfPermission(PostCreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PostCreateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
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
                    toast("Permission not granted to access images.");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                postImageView.setImageSource(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPostCreated(String... jobId) {
        showPublishingProgressDialog(false);
        finish();
    }

    @Override
    public void onPostCreateError(String... jobId) {
        showPublishingProgressDialog(false);
        toast("Failed to create post");
    }
}
