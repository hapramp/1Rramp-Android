package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.views.post.PostCreateComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCreateActivity extends AppCompatActivity implements PostCreateCallback, SteemPostCreator.SteemPostCreatorCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.postButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.post_create_component)
    PostCreateComponent postCreateComponent;
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
    private PostStructureModel postStructureModel;
    private List<String> tags;
    private String title;
    private String generated_permalink;
    private String full_permlink;
    private SteemPostCreator steemPostCreator;

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
        //HaprampPreferenceManager.getInstance().savePostDraft(content.getText().toString());
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        steemPostCreator = new SteemPostCreator();
        steemPostCreator.setSteemPostCreatorCallback(this);

    }

    private void attachListener() {

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

    private void publishPost() {

        // check connection
        if (!ConnectionUtils.isConnected(this)) {
            showConnectivityError();
            return;
        }

        if (postCreateComponent.getSelectedCommunityTags().size() == 0) {
            toast("Select Atleast One Category");
            return;
        }

        String mediaUri = postCreateComponent.getImageDownloadUrl();

        if (mediaUri != null) {
            showPublishingProgressDialog(true, "Preparing Your Post...");
            preparePost();
            showPublishingProgressDialog(true, "Pre-processing...");
            sendPostToServerForProcessing(postStructureModel);

//
//            PostCreateBody postCreateBody = new PostCreateBody(
//                    content.getText().toString(),
//                    postImageView.getDownloadUrl(),
//                    Constants.CONTENT_TYPE_POST,
//                    postCategoryView.getSelectedTags(),
//                    null);
//
//            showPublishingProgressDialog(true);
//            //gather the data
//            DataServer.createPost(postCreateBody, this);
        } else {
            toast("Your media has not been uploaded yet");
        }
    }

    private void preparePost() {

        generated_permalink = PermlinkGenerator.getPermlink();
        full_permlink = HaprampPreferenceManager.getInstance().getSteemUsername() + "/" + generated_permalink;
        Log.d("TEST", "Full Permlink " + full_permlink);
        //prepare title
        title = "";
        //prepare tags
        tags = postCreateComponent.getSelectedCommunityTags();
        //prepare post structure
        List<PostStructureModel.Data> datas = new ArrayList<>();
        datas.add(new PostStructureModel.Data(postCreateComponent.getImageDownloadUrl(), ContentTypes.DataType.IMAGE));
        datas.add(new PostStructureModel.Data(postCreateComponent.getContent(), ContentTypes.DataType.TEXT));
        postStructureModel = new PostStructureModel(datas, ContentTypes.CONTENT_TYPE_POST);

    }

    //PUBLISHING SECTION
    private void sendPostToServerForProcessing(PostStructureModel content) {

        PreProcessingModel preProcessingModel = new PreProcessingModel(full_permlink, content);

        RetrofitServiceGenerator.getService().sendForPreProcessing(preProcessingModel).enqueue(new Callback<ProcessedBodyResponse>() {
            @Override
            public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
                if (response.isSuccessful()) {
                    // send to blockchain
                    sendPostToSteemBlockChain(response.body().getmBody());
                } else {
                    toast("Something went wrong while pre-processing...");
                    showPublishingProgressDialog(false, "");
                }
            }

            @Override
            public void onFailure(Call<ProcessedBodyResponse> call, Throwable t) {
                toast("Something went wrong with network(" + t.toString() + ")");
                showPublishingProgressDialog(false, "");
            }
        });
    }

    private void sendPostToSteemBlockChain(final String body) {

        showPublishingProgressDialog(true, "Adding to Blockchain...");

        steemPostCreator.createPost(body, title, tags, postStructureModel, generated_permalink);

    }

    private void confirmServerForPostCreation() {

        showPublishingProgressDialog(true, "Sending Confirmation to Server...");

        RetrofitServiceGenerator.getService()
                .sendPostCreationConfirmation(new PostConfirmationModel(full_permlink))
                .enqueue(new Callback<ProcessedBodyResponse>() {
                    @Override
                    public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
                        if (response.isSuccessful()) {
                            toast("Server Confirmed!");
                            showPublishingProgressDialog(false, "");
                        } else {
                            toast("Failed to Confirm Server!");
                            showPublishingProgressDialog(false, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProcessedBodyResponse> call, Throwable t) {
                        toast("Something went wrong (" + t.toString() + ")");
                        showPublishingProgressDialog(false, "");
                    }
                });

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

    private void showPublishingProgressDialog(boolean show, String msg) {

        if (progressDialog != null) {
            if (show) {
                progressDialog.setMessage(msg);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            } else {
                progressDialog.hide();
            }
        } else {
            progressDialog.hide();
        }

    }

    private void showConnectivityError() {
        Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
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
               postCreateComponent.setImageResource(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPostCreated(String... jobId) {
        showPublishingProgressDialog(false, "");
        finish();
    }

    @Override
    public void onPostCreateError(String... jobId) {
        showPublishingProgressDialog(false, "");
        toast("Failed to create post");
    }

    //==================
    // STEEM POST CREATOR CALLBACK
    //==================

    @Override
    public void onPostCreatedOnSteem() {
        toast("Post Created on Blockchain");
        showPublishingProgressDialog(false, "");
        // send confirmation to server
        confirmServerForPostCreation();
    }

    @Override
    public void onPostCreationFailedOnSteem(String msg) {
        toast(msg);
        showPublishingProgressDialog(false, "");
        Log.d(PostCreateActivity.class.getSimpleName(), msg);
    }
}
