package com.hapramp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datamodels.response.ConfirmationResponse;
import com.hapramp.ui.adapters.FeaturedImageAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.editor.Editor;
import com.hapramp.datamodels.FeaturedImageSelectionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FeaturedImageItemDecorator;
import com.hapramp.utils.FontManager;
import com.hapramp.views.editor.EditorView;
import com.hapramp.views.hashtag.CustomHashTagInput;
import com.hapramp.views.post.PostCategoryView;
import com.hapramp.youtube.YoutubeVideoSelectorActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hapramp.views.editor.YoutubeInsertButtonView.YOUTUBE_RESULT_REQUEST;

public class CreateArticleActivity extends AppCompatActivity implements EditorView.OnImageUploadListener, SteemPostCreator.SteemPostCreatorCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.previewButton)
    TextView previewButton;
    @BindView(R.id.nextButton)
    TextView nextButton;
    @BindView(R.id.meta_toolbar_container)
    RelativeLayout meta_toolbarContainer;
    @BindView(R.id.backBtnFromArticleMeta)
    TextView backBtnFromArticleMeta;
    @BindView(R.id.publishButton)
    TextView publishButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.featured_image_caption)
    TextView featuredImageCaption;
    @BindView(R.id.featured_image_selectorRV)
    RecyclerView featuredImageSelectorRV;
    @BindView(R.id.community_caption)
    TextView communityCaption;
    @BindView(R.id.articleCategoryView)
    PostCategoryView articleCategoryView;
    @BindView(R.id.tagsCaption)
    TextView tagsCaption;
    @BindView(R.id.tagsInputBox)
    CustomHashTagInput tagsInputBox;
    @BindView(R.id.skills_wrapper)
    RelativeLayout skillsWrapper;
    @BindView(R.id.metaView)
    RelativeLayout metaView;
    Editor editor;
    @BindView(R.id.editorView)
    EditorView editorView;
    private ArrayList<FeaturedImageSelectionModel> insertedImages;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    FeaturedImageAdapter featuredImageAdapter;
    private String title;
    private ArrayList<String> tags;
    private PostStructureModel postStructureModel;
    private String generated_permalink;
    private String permlink_with_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        ButterKnife.bind(this);
        init();
        attachListeners();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //     saveDraft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restoreDraft();
    }

    @Override
    public void onBackPressed() {
        showExistAlert();
    }

    private void showExistAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Close")
                .setMessage("Do you want to Close Post Creation ?")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        close();
                    }
                })
                .setNegativeButton("No", null);

        builder.show();

    }

    private void close() {
        finish();
        overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                editor.insertImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == YOUTUBE_RESULT_REQUEST && resultCode == Activity.RESULT_OK) {

            String videoId = data.getStringExtra(YoutubeVideoSelectorActivity.EXTRA_VIDEO_KEY);
            editorView.insertYoutube(videoId);

        } else {
            Toast.makeText(this, "No Video Selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {

        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        backBtnFromArticleMeta.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        editor = editorView.getEditor();
        progressDialog = new ProgressDialog(this);
        editorView.setOnImageUploadListener(this);
        insertedImages = new ArrayList<>();
        featuredImageAdapter = new FeaturedImageAdapter(this);
        featuredImageSelectorRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredImageSelectorRV.setAdapter(featuredImageAdapter);
        featuredImageSelectorRV.addItemDecoration(new FeaturedImageItemDecorator());
        articleCategoryView.initCategory();

    }

    private void showPublishingProgressDialog(boolean show, String msg) {

        if (progressDialog != null) {
            if (show) {
                progressDialog.setTitle("Article Upload");
                progressDialog.setMessage(msg);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            } else {
                progressDialog.hide();
            }
        }
    }

    private void showConnectivityError() {
        Snackbar.make(toolbarContainer, "No Internet!  Article Saved To Draft ", Snackbar.LENGTH_SHORT).show();
    }

    private void feedFeaturedImageData() {

        featuredImageAdapter.setImageSelectionModels(insertedImages);

    }

    private void attachListeners() {

        // draft button
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<FeedDataItemModel> feedDataItemModels = editorView.getDataItemList();
                Intent i = new Intent(CreateArticleActivity.this, PreviewActivity.class);
                i.putParcelableArrayListExtra("data", feedDataItemModels);
                startActivity(i);

            }
        });

        //next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMetaData(true);
            }
        });

        backBtnFromArticleMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMetaData(false);
            }
        });

        // publish
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isConnected(CreateArticleActivity.this)) {
                    publishArticle();
                } else {
                    showConnectivityError();
                    saveDraft();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExistAlert();
            }
        });

    }

    private void publishArticle() {
        title = "";
        tags = (ArrayList<String>) articleCategoryView.getSelectedTags();
        if (articleCategoryView.getSelectedTags().size() == 1) {
            toast("Select Atleast One Category");
            return;
        }
        includeCustomTags(tags);
        List<FeedDataItemModel> datas = editorView.getDataItemList();
        postStructureModel = new PostStructureModel(datas, FeedDataConstants.FEED_TYPE_ARTICLE);
        sendPostToServerForProcessing(postStructureModel);
    }

    private void includeCustomTags(ArrayList<String> tags) {
        tags.addAll(tagsInputBox.getHashTags());
    }

    private void sendPostToServerForProcessing(PostStructureModel content) {
        generated_permalink = PermlinkGenerator.getPermlink();
        permlink_with_username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername()+"/"+generated_permalink;
        PreProcessingModel preProcessingModel = new PreProcessingModel(permlink_with_username, new Gson().toJson(content));
        RetrofitServiceGenerator.getService().sendForPreProcessing(preProcessingModel).enqueue(new Callback<ProcessedBodyResponse>() {
            @Override
            public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
                if (response.isSuccessful()) {
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
        SteemPostCreator steemPostCreator = new SteemPostCreator();
        steemPostCreator.setSteemPostCreatorCallback(this);
        steemPostCreator.createPost(body, title, tags, postStructureModel, generated_permalink);

    }

    private void confirmServerForPostCreation() {

        showPublishingProgressDialog(true, "Sending Confirmation to Server...");
        RetrofitServiceGenerator.getService()
                .sendPostCreationConfirmation(new PostConfirmationModel(permlink_with_username))
                .enqueue(new Callback<ConfirmationResponse>() {
                    @Override
                    public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
                        if (response.isSuccessful()) {
                            serverConfirmed();
                        } else {
                            toast("Failed to Confirm Server!");
                            showPublishingProgressDialog(false, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
                        toast("Something went wrong (" + t.toString() + ")");
                        showPublishingProgressDialog(false, "");
                    }
                });

    }

    private void serverConfirmed() {

        toast("Your post is live now.");
        showPublishingProgressDialog(false, "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }

    private void showMetaData(boolean show) {

        int vis = show ? View.VISIBLE : View.GONE;
        metaView.setVisibility(vis);

    }

    private void saveDraft() {

        toast("Article Saved To Draft");

    }

    private void restoreDraft() {
        editor.render(HaprampPreferenceManager.getInstance().getArticleAsDraft());
    }

    private void toast(String s) {

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onImageUploaded(String remotePath) {
        insertedImages.add(new FeaturedImageSelectionModel(false, remotePath));
        feedFeaturedImageData();
    }

    @Override
    public void onPostCreatedOnSteem() {
        toast("Your post will take few seconds to appear");
        showPublishingProgressDialog(false, "");
        // send confirmation to server
        confirmServerForPostCreation();
    }

    @Override
    public void onPostCreationFailedOnSteem(String msg) {
        toast(msg);
        Log.d(CreateArticleActivity.class.getSimpleName(), msg);
    }

}
